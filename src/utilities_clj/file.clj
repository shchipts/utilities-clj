;   Copyright (c) Anna Shchiptsova, IIASA. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Provides some I/O utility functions."
      :author "Anna Shchiptsova"}
 utilities-clj.file
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn walk
  "Walks through the folder files which match the specified pattern.
   Returns a java.io.File collection of the files in the folder,
   whose name matches the pattern.

   ## Usage

       (require '[utilities.file :refer :all])

       (walk source-folder #\".*\\.asc\")  "
  [folder pattern]
  (->> (io/file folder)
       file-seq
       (filter #(re-matches pattern (.getName ^java.io.File %)))
       doall))

(defn basename
  "Returns a file name without extension."
  [path]
  (->> (io/file path)
       (#(.getName ^java.io.File %))
       (#(string/split % #"\."))
       first))
