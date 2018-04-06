;   Copyright (c) Anna Shchiptsova, IIASA. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Wraps reading of file content."
      :author "Anna Shchiptsova"}
  utilities-clj.reader
  (:require [clojure.java.io :as io]
            [clojure.xml :as xml]))


(defn read-file
  "Reads entire text file and returns text by lines."
  [file-name]
  (with-open [rdr (io/reader file-name)]
    (doall (line-seq rdr))))

(defn read-xml-file
  "Reads xml file and returns its content."
  [path]
  (xml/parse (io/file path)))
