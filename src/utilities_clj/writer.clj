;   Copyright (c) Anna Shchiptsova, IIASA. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Wraps writing of file content."
      :author "Anna Shchiptsova"}
 utilities-clj.writer
  (:require [clojure.java.io :as io]
            [clojure-csv.core :as csv]))

(defn csv-file
  "Writes data to a csv file.
   If target-dir does not exists, it is created before saving. File content
   in data should be represented as a sequence of sequences of strings. The
   data is stored by rows. File is saved to [target-dir]-[file-name].
   Returns an absolute path to the target-dir.

   ## Usage

      (require '[utilities.writer :refer :all])

      (csv-file \"folder\"
                \"file.csv\"
                [[\"header 1\" \"header 2\"]
                 [\"row_1_col_1\" \"row_1_col_2\"]
                 [\"row_2_col_1\" \"row_2_col_2\"]])
  "
  [target-dir
   file-name
   data]
  (let [parent-dir (io/file target-dir)
        file (io/file parent-dir
                      file-name)]
    (if-not (.exists parent-dir)
      (.mkdir parent-dir))
    (with-open [writer (io/writer file)]
      (spit writer
            (csv/write-csv data)))
    (identity (.getAbsolutePath parent-dir))))

(defn txt-file
  "Writes data to a text file.
   If target-dir does not exists, it is created before saving. File content
   in lines should be represented as a sequence of strings. File is saved
   to [target-dir]-[file-name].
   Returns an absolute path to the target-dir.

   ## Usage

      (require '[utilities.writer :refer :all])

      (txt-file \"folder\"
                \"file.asc\"
                [\"line 1\"
                 \"line 2\"
                 \"line 3\"])"
  [target-dir
   file-name
   lines]
  (let [parent-dir (io/file target-dir)
        file (io/file parent-dir
                      file-name)]
    (if-not (.exists parent-dir)
      (.mkdir parent-dir))
    (with-open [file-writer (io/writer file)]
      (doseq [line lines]
        (.write file-writer ^java.lang.String line)
        (.newLine ^java.io.BufferedWriter file-writer)))
    (identity (.getAbsolutePath parent-dir))))
