(ns vanity-assembly.core.config)

;;----------------------------------------------
;; Config related functions
;;----------------------------------------------
(def ^{:private true} config
  (read-string (slurp "./config.edn")))

(defn get-config [& args]
  (try
    (loop [current config index 0]
      (if (== index (count args))
        current
        (recur (current (nth args index)) (inc index))))
    (catch Exception e nil)))