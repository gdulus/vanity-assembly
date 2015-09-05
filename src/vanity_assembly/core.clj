(ns vanity-assembly.core
  (:require [liberator.core :refer [resource defresource]]
            [liberator.representation :refer [ring-response]]
            [clojure.string :as string]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes GET]]
            [vanity-assembly.core.rest :refer [p->n p->s]]
            [vanity-assembly.core.neo4j :refer [with-connection]]
            [vanity-assembly.domain.core :refer [list-and-count-nodes find-node-by-id search-nodes]]))

(defroutes app

           (GET "/" [] (resource :available-media-types ["application/json"]
                                 :handle-ok (fn [ctx] (ring-response {} {:status 404}))))

           ;; ----------------------------------------------------------------------------------------------

           (GET "/search" [] (resource :available-media-types ["application/json"]
                                       :handle-ok (fn [ctx] (let [node-type (p->s ctx "type")
                                                                  node-name (p->s ctx "name")]
                                                                (if (or (string/blank? node-name) (string/blank? node-type))
                                                                    (ring-response {} {:status 404})
                                                                    {:data (search-nodes node-type node-name)})))))

           ;; ----------------------------------------------------------------------------------------------

           (GET "/node" [] (resource :available-media-types ["application/json"]
                                     :handle-ok (fn [ctx] (let [id (p->n ctx "id")]
                                                            (if (nil? id)
                                                              (ring-response {} {:status 404})
                                                              (find-node-by-id id))))))

           ;; ----------------------------------------------------------------------------------------------

           (GET "/groups" [] (resource :available-media-types ["application/json"]
                                       :handle-ok (fn [ctx] (let [page (p->n ctx "page" 0)]
                                                              (list-and-count-nodes "Group" page 50)))))

           ;; ----------------------------------------------------------------------------------------------

           (GET "/celebrities" [] (resource :available-media-types ["application/json"]
                                            :handle-ok (fn [ctx] (let [page (p->n ctx "page" 0)]
                                                                   (list-and-count-nodes "Celebrity" page 50)))))

           ;; ----------------------------------------------------------------------------------------------

           (GET "/aliases" [] (resource :available-media-types ["application/json"]
                                        :handle-ok (fn [ctx] (let [page (p->n ctx "page" 0)]
                                                               (list-and-count-nodes "Alias" page 50))))))


(def handler
  (-> app
      wrap-params))

