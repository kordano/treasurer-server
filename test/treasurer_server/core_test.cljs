(ns treasurer-server.core-test
  (:require [cljs.test :refer-macros [deftest is testing async run-tests]]))

(enable-console-print!)

(defmethod cljs.test/report [:cljs.test/default :end-run-tests] [m]
  (if (cljs.test/successful? m)
    (println "Success!")
    (println "FAIL")))

(deftest simple-test
  (testing "calculates correct instance"
    (is (= 42 (+ 9 10 11 12)))))
