(ns http-client-template.core
  (:require [http-client-template.datasources.jsonplaceholder :as jp]))

(defn -main [& args]
  (jp/create-post {:user-id 5 :title "hello world" :body "sup"})
  (jp/update-post {:id 1 :user-id 6 :title "hello world updated" :body "sup updated"})
  (jp/get-post-by-id 1)
  (jp/delete-post 1))

(-main)
