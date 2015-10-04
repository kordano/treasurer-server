(ns treasurer-server.core
  (:require [cljs.nodejs :as node]))

(def INITIAL_STATE (atom {}))

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
  (get-in @state [:entries]))


(defn dispatch
  "Dispatches incoming actions to reducers"
  ([action] (dispatch INITIAL_STATE action))
  ([state {:keys [type data]
           :as action
           :or {:data nil}}]
   (case type
     :add-entry (add-entry state data)
     :get-all (get-all state)
     :unrelated)))


(defn socket-dispatch
  "Dispatches incoming socket data"
  [socket]
  (.on socket "event" (fn [data] (dispatch state data)))
  (.on socket "disconnect" (fn [] (println "socket" socket "closed!"))))



(defn start-server
  "Initializes sockets and listens on them"
  [state & {:keys [port] :or {port 3000}}]
  (let [server (.createServer (node/require "http"))
        io ((node/require "socket.io") server)]
    (.on io "connection" socket-dispatch)
    (.listen server port)))

(defn -main
  "Entrypoint"
  []
  (let [port 3000]
    (start-server INITIAL_STATE :port port)
    (println (str "Server started on localhost:" port))))

(set! *main-cli-fn* -main)
