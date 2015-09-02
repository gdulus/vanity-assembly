(defproject vanity-assembly "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler vanity-assembly.core/handler}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clojurewerkz/neocons "3.1.0-beta3"]
                 [jarohen/nomad "0.7.1"]
                 [liberator "0.13"]
                 [compojure "1.3.4"]
                 [ring/ring-core "1.2.1"]])