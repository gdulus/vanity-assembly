(ns vanity-assembly.domain.celebrity
  (:require
    [vanity-assembly.core.neo4j :refer [with-connection create-node create-relation get-by-id]]))

;; CREATE CONSTRAINT ON (c:Celebrity) ASSERT c.ident IS UNIQUE

;;----------------------------------------------

(defn create-celebrity [name celebirty-ident]
  "Creates Celebirty node"
  (with-connection #(create-node % "Celebrity" {:name name :ident celebirty-ident})))

;;----------------------------------------------

(defn create-celebrity-alias [celebirty-id name alias-ident]
  "Creates Alias node for Celebrity node. Creates connection between"
  (with-connection #(let [celebrity (get-by-id % celebirty-id)
                          alias (create-node % "Alias" {:name name :ident alias-ident})
                          rel (create-relation % celebrity alias :is)]
                     rel)))

;;----------------------------------------------

(defn create-celebrity-connection [source-celebirty-id target-celebirty-id relation]
  "Creates Alias node for Celebrity node. Creates connection between"
  (with-connection #(let [source-celebirty (get-by-id % source-celebirty-id)
                          target-celebirty (get-by-id % target-celebirty-id)
                          rel (create-relation % source-celebirty target-celebirty relation)]
                     rel)))


;;(create-celebrity "Celebrity 1" 1) 541
;;(create-celebrity-alias 541 "Alias 3" 4)
;;(create-celebrity "Celebrity 2" 2) 545
;;(create-celebrity-alias 545 "Alias 2-1" 10)
;;(create-celebrity-alias 545 "Alias 2-2" 11)
;;(create-celebrity-connection 541 545 :wife)

