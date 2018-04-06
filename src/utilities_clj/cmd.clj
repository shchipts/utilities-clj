;   Copyright (c) Anna Shchiptsova, IIASA. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Provides some options for a command line program."
    :author "Anna Shchiptsova"}
  utilities-clj.cmd
  (:require [clojure.string :as string]
            [clojure.tools.cli :as cli]))


(def ^{:private true} cli-options
  "Default command line options."
  [["-t" "--trace" "Print stack trace"
    :id :trace
    :default false]
   ["-h" "--help" "Print command help"]])

(defn- usage
  "Returns program usage description."
  [short-description
   options-summary
   arguments-summary]
  (string/join \newline
               [short-description
                ""
                (->> ((juxt #(map first (:required %))
                            #(map (fn[[arg _]]
                                    (str "[" arg "]"))
                                  (:optional %)))
                      arguments-summary)
                     (apply concat)
                     (string/join \space)
                     (str "Usage: program-name [options] "))
                ""
                "Options:"
                options-summary
                ""
                "Arguments:"
                (->> ((juxt :required
                            :optional)
                      arguments-summary)
                     (apply concat)
                     (map (fn[[arg desc]]
                            (str "  " arg \tab desc)))
                     (string/join \newline))
                ""
                "Please refer to the manual page for more information."]))

(defn- exit
  "Terminates program execution."
  [status messsage]
  (do
    (println messsage)
    (System/exit status)))


(defn terminal
  "Executes program in terminal.

   Runs fn in (:execute) with passed command line arguments (:args).

   Supplies program with options (:opts).
   Supported default command line options are
       -h (--help)   -t (--trace)

   Wraps unhandled exceptions. If -t is set, returns full stack trace.
   Otherwise, returns only exception message.

   Help provides short program description (:short-description),
   description of command line options, description of command line
   arguments, etc.

   The format for description of command line arguments
   (:args-description) is:
       {:required
        [[argument-name1 argument-description1]
         [argument-name2 argument-description2]
          ...]
        :optional
        [[optional-argument-name1 argument-description1]
         [optional-argument-name2 argument-description2]
          ...]}

   ## Usage

       (require '[utilities.cmd :refer :all])

       (def arguments
            {:short-desc \"Program short description goes here.\"
             :args '(arg1 arg2 ... [optional-arg1] [optional-arg2] ...)
             :opts '([\"-o\" \"--option\" \"Set some option\"
                      :id :opt])
             :args-desc {:required
                         [[\"arg1\" \"Description for arg1 goes here.\"]
                          [\"arg2\" \"Description for arg2 goes here.\"]
                          ...]
                         :optional
                         [[\"optional-arg1\" \"Description for optional-arg1 goes here.\"]
                          [\"optional-arg2\" \"Description for optional-arg2 goes here.\"]
                          ...]}
             :execute (fn[args](println \"Hello, world!\"))})

       (terminal arguments)
  "
  [{program-short-desc :short-desc
    args :args
    opts :options
    cli-args :args-desc
    execute :execute}]
  (let [{:keys [options
                arguments
                errors
                summary]}
        (cli/parse-opts args
                        (concat opts cli-options))
        usage-method
        #(usage program-short-desc
                summary
                cli-args)]
    (cond
     (:help options) (exit 1 (usage-method))
     (< (count arguments)
        (count (:required cli-args))) (exit 1 (usage-method))
     (> (count arguments)
        (->> ((juxt :required
                    :optional)
              cli-args)
             (map count)
             (apply +))) (exit 1 (usage-method))
     errors (exit 1 (string/join \newline errors))
     :else
     (try
       (execute arguments options)
       (catch Exception e (if (:trace options)
                            (throw e)
                            (println (str "Error: " (.getMessage e)))))
       (finally (shutdown-agents))))))
