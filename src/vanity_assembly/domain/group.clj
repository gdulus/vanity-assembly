(ns vanity-assembly.domain.group
  (:require
    [vanity-assembly.core.neo4j :refer [with-connection create-node create-relation execute-query fetch-query get-by-id]]
    [vanity-assembly.domain.celebrity :refer [find-celebrity-by-ident]]))

;; CREATE CONSTRAINT ON (c:Group) ASSERT c.ident IS UNIQUE

;;----------------------------------------------

(defn create-group [name]
  "Creates Group node"
  (with-connection #(create-node % "Group" {:name name})))

;;----------------------------------------------

(defn add-celebrit-to-group [celebirty-id group-id]
  "Creates Alias node for Celebrity node. Creates connection between"
  (with-connection #(let [group (get-by-id % group-id)
                          celebrity (get-by-id % celebirty-id)
                          rel (create-relation % celebrity group :belongsto)]
                     rel)))


;;(dotimes [n 100] (create-group (str "Group " n)))
;;(create-group "Group 1") 539
;;(create-group "Group 2") 540
;;(add-celebrit-to-group 541 540)