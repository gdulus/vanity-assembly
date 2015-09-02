(ns vanity-assembly.core.neo4j
  (:require [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.labels :as nl]
            [clojurewerkz.neocons.rest.nodes :as nn]
            [clojurewerkz.neocons.rest.cypher :as cy]
            [clojurewerkz.neocons.rest.relationships :as nrl]
            [vanity-assembly.core.config :refer [get-config]]))

;;----------------------------------------------

(defn with-connection [worker]
  "Prepares Neocons connection"
  (let [conn (nr/connect (get-config :neo4j :connection))]
    (worker conn)))

;;----------------------------------------------

(defn create-node [conn type data]
  "Creates noed and label it"
  (let [node (nn/create conn data)
        label (nl/add conn node type)]
    node))

;;----------------------------------------------

(defn create-relation [conn from to type]
  "Create connection between 2 nodes"
  (nrl/create conn from to type))

;;----------------------------------------------

(defn- prepare-query [query params]
  (if (= (count params) 0)
    query
    (let [escaped-parmas (map #(if (instance? String %) (str "'" % "'") %) params)
          query (apply format query escaped-parmas)]
      query)))

(defn execute-query [conn query & params]
  "API for query with Cypher"
  (let [query (prepare-query query params)]
    (cy/tquery conn query)))

(defn- fetch [conn result]
  (if (nil? result)
    result
    (nn/get conn (get result "id"))))

(defn fetch-query [conn query & params]
  "Fetch Neocons object base on query result. Query need to return only id"
  (let [result (execute-query conn query params)]
    (map #(fetch conn %) result)))

;;----------------------------------------------

(defn get-by-id [conn id]
  "Wrapper around clojurewerkz get function"
  (nn/get conn id))