(ns treasurer-server.core
  (:require [cljs.nodejs :as node]))

(def server-state (atom {}))

(node/enable-util-print!)

(defn add-entry
  "Adds new element to entries in state"
  [state entry]
  (swap! state update-in [:entries] #(if (= (type %) cljs.core/PersistentVector)
                                       (conj % entry)
                                       [entry]))
  state)

(defn get-all
  "Retrieves all elements from state entries"
  [state]
  (get-in @state [:entries])
