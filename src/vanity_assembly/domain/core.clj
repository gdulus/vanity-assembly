(ns vanity-assembly.domain.core
    (:require [vanity-assembly.core.neo4j :refer [with-connection execute-query]]
              [clojure.string :as string]))

(defn list-nodes
  ([conn node-type skip limit]
   (execute-query conn (str "MATCH (n:" node-type ") RETURN id(n) as id, n.name as name SKIP %s LIMIT %s") skip limit))
  ([node-type skip limit]
   (with-connection #(list-nodes % node-type skip limit))))

;;----------------------------------------------

(defn count-nodes
  ([conn node-type]
   (get (first (execute-query conn (str "MATCH (n:" node-type ") RETURN count(n) as i"))) "i"))
  ([node-type]
   (with-connection #(count-nodes % node-type))))

;;----------------------------------------------

(defn list-and-count-nodes [node-type page limit]
  (let [skip (* page limit)]
    (with-connection (fn [conn] {:page  page
                                 :limit limit
                                 :total (count-nodes conn node-type)
                                 :data  (list-nodes conn node-type skip limit)}))))

;;----------------------------------------------

(defn find-node-by-id [node-id]
  "Creates Alias node for Celebrity node. Creates connection between"
  (with-connection #(execute-query % "START f = node(%s)
                                      MATCH (f)<-[*0..1]->(t)
                                      RETURN id(f) as fid, labels(f) as flabel, f.name as fname, id(t) as tid, labels(t) as tlabel, t.name as tname" node-id)))

;;----------------------------------------------

(defn search-nodes [node-type name]
    "Searches for node by tape and name"
    (with-connection #(execute-query % (str "MATCH (n:" (string/capitalize node-type) ") WHERE lower(n.name) =~ lower('.*" name ".*') RETURN id(n) as id, n.name as name"))))