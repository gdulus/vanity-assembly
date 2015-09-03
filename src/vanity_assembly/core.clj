(ns vanity-assembly.core
  (:require [liberator.core :refer [resource defresource]]
            [liberator.representation :refer [ring-response]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [defroutes GET]]
            [vanity-assembly.core.rest :refer [p->n]]
            [vanity-assembly.core.neo4j :refer [with-connection]]
            [vanity-assembly.domain.core :refer [list-and-count-nodes find-star-by-id]]))

(defroutes app

           (GET "/" [] (resource :available-media-types ["application/json"]
                                 :handle-ok (fn [ctx] (ring-response {} {:status 404}))))

           ;; ----------------------------------------------------------------------------------------------

           (GET "/node" [] (resource :available-media-types ["application/json"]
                                     :handle-ok (fn [ctx] (let [id (p->n ctx "id")]
                                                            (if (nil? id)
                                                              (ring-response {} {:status 404})
                                                              (find-star-by-id id))))))

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

