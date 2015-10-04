(ns treasurer-server.core-test
  (:require [cljs.test :refer-macros [deftest is testing async run-tests]]
            [treasurer-server.core :refer [add-entry get-all dispatch]]))

(enable-console-print!)

(deftest basic-interactions
  
  (testing "entries are only vectors"
    (let [state (atom {})
          new-entry {:ts "2015-01-01" :company "Aldi" :amount 42}
          next-state (add-entry state new-entry)]
        (is (= cljs.core/PersistentVector
               (-> next-state deref :entries type)))))
  
  (testing "adds new element to the entry vector of the state"
    (let [state (atom {})
          new-entry {:ts "2015-01-01" :company "Aldi" :amount 42}
          next-state (add-entry state new-entry)]
      (is (= @next-state
             {:entries [{:ts "2015-01-01" :company "Aldi" :amount 42}]}))))
  
  (testing "retrieves all elements of the state's entries"
    (let [state (atom {:entries [{:ts "2015-01-01" :company "Aldi" :amount 42}
                                 {:ts "2015-01-02" :company "Rewe" :amount 666}]})
          elements (get-all state)]
      (is (= elements
             [{:ts "2015-01-01" :company "Aldi" :amount 42}
              {:ts "2015-01-02" :company "Rewe" :amount 666}])))))


(deftest basic-reducers
  
  (testing "handles :add-entry"
    (let [initial-state (atom {})
          action {:type :add-entry :data {:ts "2015-01-01" :company "Aldi" :amount 42}}
          next-state (dispatch initial-state action)]
      (is (= @next-state
             {:entries [{:ts "2015-01-01" :company "Aldi" :amount 42}]}))))
  
  (testing "handles :get-all"
    (let [initial-state (atom
                         {:entries [{:ts "2015-01-01" :company "Aldi" :amount 42}
                                    {:ts "2015-01-02" :company "Rewe" :amount 666}]})
          action {:type :get-all}
          elements (dispatch initial-state action)]
      (is (= elements
             [{:ts "2015-01-01" :company "Aldi" :amount 42}
              {:ts "2015-01-02" :company "Rewe" :amount 666}]))))
  
  (testing "has an initial state"
    (let [action {:type :add-entry :data {:ts "2015-01-01" :company "Aldi" :amount 42}}
          next-state (dispatch action)]
      (is (= @next-state
             {:entries [{:ts "2015-01-01" :company "Aldi" :amount 42}]}))))
  
  (testing "can be used with reducers"
    (let [actions [{:type :add-entry :data {:ts "2015-01-01" :company "Aldi" :amount 42}}
                   {:type :add-entry :data {:ts "2015-01-01" :company "Aldi" :amount 43}}
                   {:type :add-entry :data {:ts "2015-01-01" :company "Aldi" :amount 44}}]
          final-state (reduce dispatch (atom {}) actions)]
      (is (= @final-state {:entries [{:ts "2015-01-01" :company "Aldi" :amount 42}
                                     {:ts "2015-01-01" :company "Aldi" :amount 43}
                                     {:ts "2015-01-01" :company "Aldi" :amount 44}]})))))

