;   Copyright (c) Anna Shchiptsova, IIASA. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Type conversion to string using a custom format."
      :author "Anna Shchiptsova"}
 utilities-clj.format
  (:require [utilities-clj.string-formatter :as string-formatter]))

(defn double-to-str
  "Formats a string from a floating-point number. Value is converted
   to string with the number of decimal places equal to digits. Formatting
   is independent from locale with point used as decimal separator.
   Returns string in the floating-point format: [+-]([0-9]*[.])?[0-9]+

   ## Usage

       (require '[utilities.format :refer :all])

       (double-to-str 1.9 4)
       => \"1.9000\"

       (double-to-str -45.688 2)
       => \"-45.69\"

       (double-to-str +45.688 2)
       => \"45.69\"
  "
  [value digits]
  (-> (str "%."
           digits
           "f")
      (format value)
      string-formatter/to-numeric))
