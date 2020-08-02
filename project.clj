(defproject http-client-template "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.martinklepsch/clj-http-lite "0.4.3"]
                 [org.clojure/data.json "1.0.0"]
                 [yogthos/config "1.1.7"]]
  :repl-options {:init-ns http-client-template.core}
  :profiles {:prod {:resource-paths ["config/pro"]}
             :dev  {:resource-paths ["config/dev"]}})
