(ns http-client-template.core
  (:require [http-client-template.datasource.json-placeholder.datasource :as datasource]))

(defn -main [& args]
  ;; (prn (datasource/create {:user-id 5 :title "hello world" :body "sup"}))
  (datasource/create {:user-id 5 :title "hello world" :body "sup"}))

(-main)

