(ns utilities-clj.floating-point-comparison-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [clojure.math.numeric-tower :as math]
            [utilities-clj.floating-point-comparison :refer :all]))

;;; tests

(deftest real-numbers-should-be-tested-for-equality
  (testing "Real numbers should be considered equal
            if they are sufficiently close to each other."
    (
     ; Arrange
     let [x 0.69 y (- (+ 66 0.69) 66)]

       ; Assert
       (is (real= x y))
       (is (real= y x)))))

(deftest big-real-numbers-should-be-tested-for-equality-relative-to-their-range
 (testing "Big real numbers should be considered equal
           if their relative error is sufficiently small."
   (
    ; Arrange
    let [x 1.23456e38 y (* 1.23456 (math/expt 10 38))]

      ; Assert
      (is (real= x y))
      (is (real= y x)))))

(deftest small-real-numbers-should-be-tested-for-equality-directly
  (testing "Small real numbers should be considered equal
            if their absolute error is sufficiently small."
    (
     ; Arrange
     let [x 0.00000000002 y 0.00000000001]

       ; Assert
       (is (real= x y)))))

(deftest vectors-of-real-numbers-should-be-tested-for-equality
  (testing "Vectors of real numbers should be considered equal
            if their components are sufficiently close to each other."
    (
     ; Arrange
     let [x [0.69 0.00000000002] y [(- (+ 66 0.69) 66) 0.00000000001]]

       ; Assert
       (is (real= x y))
       (is (real= y x)))))

(deftest real<-test
  (testing "A real number should be considered less than the other number
            if it is sufficiently smaller."
    ; Assert
    (is (not (real< 1.3333333333 (/ 4.0 3.0))))
    (is (not (real< 1.3333333333e38 (* (/ 4.0 3.0) (math/expt 10 38)))))
    (is (not (real<  0.00000000001  0.00000000009)))))

(deftest real>-test
  (testing "A real number should be considered greater than the other number
            if it is sufficiently bigger."
    ; Assert
    (is (not (real> (/ 4.0 3.0) 1.3333333333)))))

(deftest real<=-test
  (testing "A real number should be considered less than or equal to the other number
            if it is smaller or sufficiently close to it."
    ; Assert
    (is (real<= (/ 4.0 3.0) 1.3333333333))))

(deftest real>=-test
  (testing "A real number should be considered greater than or equal to the other number
            if it is bigger or sufficiently close to it."
    ; Assert
    (is (real>= 1.3333333333 (/ 4.0 3.0)))))

(deftest real-numbers-should-not-be-equal-with-nil
  (testing "Comparison with nil should return false."
    ; Assert
    (is (not (real= nil 0.5)))
    (is (not (real= 0.5 nil)))))

(deftest vectors-of-different-size-should-not-be-equal
  (testing "Comparison of vectors of different size should return false."
    ; Assert
    (is (not (real= [1 1] [1 1 1])))
    (is (not (real= [1 1 1] [1 1])))))


;;; test grouping

(deftest real=-test
  (testing "Comparison of floating point numbers:\n"
    (real-numbers-should-be-tested-for-equality)
    (big-real-numbers-should-be-tested-for-equality-relative-to-their-range)
    (small-real-numbers-should-be-tested-for-equality-directly)
    (vectors-of-real-numbers-should-be-tested-for-equality)
    (real-numbers-should-not-be-equal-with-nil)
    (vectors-of-different-size-should-not-be-equal)))


;;; tests in the namespace

(defn test-ns-hook
  "summary: Explicit definition of tests in the namespace.
   returns: Explicit definition of tests in the namespace."
  []
  (real=-test)
  (real<-test)
  (real>-test)
  (real<=-test)
  (real>=-test))
