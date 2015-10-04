(ns treasurer-server.core-test
  (:require [cljs.test :refer-macros [deftest is testing async run-tests]]
            [treasurer-server.core :refer [add-entry get-all]]))

(enable-console-print!)

(defmethod cljs.test/report [:cljs.test/default :end-run-tests] [m]
  (if (cljs.test/successful? m)
    (println "Success!")
    (println "FAIL")))

(deftest basic-insertions
  (testing "adds new element to the entry vector of the state"
    (let [state (atom {})
          new-entry {:ts "2015-01-01" :company "Aldi" :amount 42}
          next-state (add-entry state new-entry)]
      (is (= cljs.core/PersistentVector (-> next-state deref :entries type)))
      (is (= @next-state {:entries [{:ts "2015-01-01" :company "Aldi" :amount 42}]})))))

(deftest basic-extractions
  (testing "retrieves all elements of the state's entries"
    (let [state (atom {:entries [{:ts "2015-01-01" :company "Aldi" :amount 42}
                                 {:ts "2015-01-02" :company "Rewe" :amount 666}]})
          elements (get-all state)]
      (is (= elements [{:ts "2015-01-01" :company "Aldi" :amount 42}
                       {:ts "2015-01-02" :company "Rewe" :amount 666}])))))
