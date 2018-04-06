;   Copyright (c) Anna Shchiptsova, IIASA. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   MIT License (http://opensource.org/licenses/MIT)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.


(ns ^{:doc "Epsilon comparison of floating point numbers."
      :author "Anna Shchiptsova"}
  utilities-clj.floating-point-comparison
  (:require [clojure.math.numeric-tower :as math]))


(def ^:private epsilon
  "Defines float precision for operations with real numbers (7 decimal digits)."
  0.0000001)


(defn- relative-diff
  "Returns difference between the first real number
   and the second real number relative to their maximum."
  [x y]
  (/ (- x y) (max (math/abs x) (math/abs y))))


(defn real=
  "Determines whether two real numbers (or vectors of real numbers)
   are equal. Returns true if either absolute difference or relative
   difference of x and y is smaller than epsilon.

   ## References
       [1] http://floating-point-gui.de/"
  [x y]
  (if (or (nil? x) (nil? y))
    false
    (if (and (number? x) (number? y))
      (recur (vector x) (vector y))
      (if (not= (count x) (count y))
        false
        (if (empty? x)
          true
          (let [number1 (first x) number2 (first y)]
            (if-not (if (<= (math/abs (- number1 number2)) epsilon)
                      true
                      (<= (math/abs (relative-diff number1 number2)) epsilon))
              false
              (recur (rest x) (rest y)))))))))

(defn real<
  "Determines whether the first real argument is less than the second one."
  [x y]
  (if (real= x y)
    false
    (> (relative-diff y x) epsilon)))

(defn real>
  "Determines whether the first real argument is greater than the second one."
  [x y]
  (real< y x))

(defn real<=
  "Determines whether the first real argument is less than or equal
   to the second one."
  [x y]
  (not (real> x y)))

(defn real>=
  "Determines whether the first real argument is greater than or equal
   to the second one."
  [x y]
  (not (real< x y)))
