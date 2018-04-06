;   Copyright (c) Anna Shchiptsova, IIASA. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Provides benchmarking functionality."
      :author "Anna Shchiptsova"}
  utilities-clj.benchmark
  (:require [clojure.tools.namespace.find :as namespaces]
            [clojure.java.io :as io]
            [clojure.java.classpath :as cp]
            [clojure.string :as string]
            [cemerick.pomegranate :as pomegranate]
            [utilities-clj.writer :as writer]
            [criterium.core :as criterium]))


(def ^:private benchmarks-dir
  "Defines project directory with benchmark tests."
  "benchmarks")

(defn- benchmark-name
  "Composes a function name with namespace.
   Format: ns[separator]function-name,
      with no separator default format is ns/function-name"
  ([function]
   (benchmark-name function "/"))
  ([function separator]
   (let [{parent-ns :ns
          fn-name :name}
         (meta function)]
     (str (ns-name parent-ns)
          separator
          fn-name))))

(defn- load-benchmarks
  "Load benchmark test namespaces. If [namespaces] is empty,
   loads all namespaces in the benchmark folder."
  [namespaces]
  (do
    (pomegranate/add-classpath benchmarks-dir)
    (doto (->> (io/file benchmarks-dir)
               namespaces/find-namespaces-in-dir
               ((fn[coll]
                  (if-not (empty? namespaces)
                    (filter (set namespaces) coll)
                    coll))))
      (#(when (seq %)
          (apply require %))))))

(defn- write-edn
  "Write to disk an edn file with results of benchmarking.
   Saves [data] content to benchmarks/results folder/[ns$function.edn]."
  [data function]
  (writer/txt-file (io/file benchmarks-dir
                            "results")
                   (str (benchmark-name function "$")
                        ".edn")
                   (vector (prn-str data))))

(defn- print-benchmarks
  "Prints results of benchmarking to the output stream."
  [data-with-function-all]
  (do
    (println)
    (println "============================ Benchmark output ============================")
    (println "*** format - execution time (s): mean, variance, lower and upper quantiles")
    (println)
    (doseq [[data function] data-with-function-all]
      (do
        (println (str "Benchmark test: "
                      (benchmark-name function)))
        (apply println
               (map #(format "%10.4f "
                             (first (% data)))
                    [:mean
                     :variance
                     :lower-q
                     :upper-q]))
        (println)))
    (println "==========================================================================")
    (println)))

(defn- parse-args
  "Parses command line arguments into benchmark options."
  [args]
  (zipmap [:quick?
           :namespaces]
          ((juxt
            #((fnil identity false)
              (some (set [":quick"]) %))
            #(->> (remove (set [":quick"]) %)
                  (map
                   (fn[arg]
                     (->> (string/split arg  #"/")
                          (map symbol)
                          ((juxt first second)))))
                  flatten
                  (apply hash-map)))
           args)))


(defmacro defbenchmark
  "Generate benchmark test.

   The function to benchmark is defined by [benchmark-name] and
   [docstring] as a function name and docstring, [arg-list] as
   function input arguments and [body] as a function body.
   The return value of the [setup] function is passed to this
   function as arguments during benchmarking. The setup function
   should always return collection of arguments.

   After execution, your benchmark test will return
   benchmark results from Criterium measurements.

   A benchmark test has one argument. If it is passed
   as true, then benchmarking is done with criterium.core/quick-benchmark,
   otherwise, it is done with criterium.core/benchmark.

   Put your benchmark tests into a \"benchmarks\" directory
   in your project root.

   ## Usage

      (:require [utilities.benchmark :refer :all])

      (defbenchmark benchmark-sum
       \"Sum of arguments.\"
       [x y]
       (identity [1 2])
       (+ x y))

      (benchmark-sum true)

   ## References
       [1] https://github.com/hugoduncan/criterium "
  [benchmark-name
   doc-string
   arg-list
   setup
   body]
  `(defn ~(with-meta (symbol benchmark-name)
            {:benchmark true
             :doc doc-string})
     [~'quick?]
     (let [~'args ~setup]
       (if ~'quick?
         (criterium/quick-benchmark (apply (fn ~(vec arg-list)
                                             (~@body))
                                           ~'args)
                                    nil)
         (criterium/benchmark (apply (fn ~(vec arg-list)
                                       (~@body))
                                     ~'args)
                              nil)))))

(defn bench
  "Run the project's benchmark tests.

   To add this task to a project create a \"leiningen\" directory in the your
   project root/src. Then you can put your tasks in there, like
   src/leiningen/your-task-name.clj. Next, add a function named \"your-task-name\"
   in the namespace \"leiningen.your-task-name\", which takes a project argument
   containing information defined in defproject and command-line arguments.
   Call this task from \"your-task-name\" with command-line arguments.


   Arguments to this task must be a list of namespaces with benchmark tests and
   benchmark options. If no namespace is specified, all namespaces in the
   \"benchmarks\" directory will be loaded.

   Recognized formats of namespace arguments: \"namespace-name\", \"namespace-name/benchmark-test-name\"
    \"namespace-name\"
      all benchmark tests in this namespace will be executed,
    \"namespace-name/benchmark-test-name\"
      specified benchmark test in this namespace will be executed.

   Recognized benchmark options: :quick
    :quick forces benchmark tests to be run with criterium.core/quick-benchmark,
      with no argument benchmark tests are run with criterium.core/benchmark.


   Define your benchmark tests using the defbenchmark macro.
   Put them into the \"benchmarks\" directory in your project root.

   ## References
       [1] https://nakkaya.com/2010/02/25/writing-leiningen-plugins-101/ "
  [& args]
  (let [cmd-args (parse-args (list* args))]
    (->> (:namespaces cmd-args)
         keys
         load-benchmarks
         (map
          (fn[v]
            (->> (the-ns v)
                 ((juxt
                   ns-publics
                   #(get (:namespaces cmd-args)
                         (ns-name %))))
                 ((fn[[coll k]]
                    (if (nil? k)
                      (vals coll)
                      (list (get coll k))))))))
         (apply concat)
         (filter #(:benchmark (meta %)))
         (map
          (juxt
           #(do
              (println
               (str "Running benchmarking in "
                    (benchmark-name %)
                    "..."))
              (let [output (% (:quick? cmd-args))]
                (println)
                (identity output)))
           identity))
         doall
         ((juxt print-benchmarks
                #(doall
                  (map
                   (fn[[d m]]
                     (write-edn d m))
                   %)))))))
