;   Copyright (c) Anna Shchiptsova, IIASA. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns ^{:doc "Formats data stored in a string."
      :author "Anna Shchiptsova"}
  utilities-clj.string-formatter
  (:require [clojure.string :as string]))


(defn to-numeric
  "Changes decimal separator in a string numeric value to a point.
   It is expected that value matches [+-]([0-9]*[decimal-separator])?[0-9]+
   where decimal-separator can be any char except [0-9], + and -.

   Returns numeric string with point as decimal separator,
   which matches [+-]([0-9]*[.])?[0-9]+

   ## Usage

      (require '[utilities.string-formatter :refer :all])

      (to-numeric \"123,567\")
      => \"123.567\"

      (to-numeric \"-5,89\")
      => \"5.89\"

      (to-numeric \"1\")
      => \"1\"

      (to-numeric \",02\")
      => \".02\"

      (to-numeric \"+8,2\")
      => \"+8.2\"

      (to-numeric \"-1\")
      => \"-1\"

      (to-numeric \"5,\")
      => \"5.\"

      (to-numeric \"4;5\")
      => \"4.5\"
  "
  [value]
  (string/replace value
                  #"[^+\-0-9]"
                  "."))
