(ns vanity-assembly.core.rest
    (:require [clojure.string :as string]))

;; ----------------------------------------------------------------------------------------------

(defn p->s
    "Returns string parameter from context"
    ([ctx param default]
     (let [value (get-in ctx (conj [:request :params] param))]
         (if (string/blank? value) default value)))
    ([ctx param]
     (p->s ctx param nil)))

;; ----------------------------------------------------------------------------------------------

(defn p->n
    "Returns number parameter from context"
    ([ctx params default]
     (let [val (p->s ctx params)]
         (if (nil? val)
             default
             (read-string val))))
    ([ctx params]
     (p->n ctx params nil)))