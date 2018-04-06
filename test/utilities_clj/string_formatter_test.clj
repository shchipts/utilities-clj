(ns utilities-clj.string-formatter-test
  (:require [clojure.test :refer :all]
            [utilities-clj.string-formatter :refer :all]))


;;; tests

(deftest to-numeric-test
  (testing "Numeric string should be converted to the format with point as decimal places separator."
    (
     ; Act
     let [s1 (to-numeric "123,567")
          s2 (to-numeric "-5,89")
          s3 (to-numeric "1")
          s4 (to-numeric ",02")
          s5 (to-numeric "+8,2")
          s6 (to-numeric "-1")
          s7 (to-numeric "5,")
          s8 (to-numeric "4;5")]

       ; Assert
       (is (= s1 "123.567"))
       (is (= s2 "-5.89"))
       (is (= s3 "1"))
       (is (= s4 ".02"))
       (is (= s5 "+8.2"))
       (is (= s6 "-1"))
       (is (= s7 "5."))
       (is (= s8 "4.5")))))
