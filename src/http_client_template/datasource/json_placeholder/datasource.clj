(ns http-client-template.datasource.json-placeholder.datasource
  (:require [http-client-template.datasource.client :as http]
            [http-client-template.datasource.json-placeholder.converter :as conv]))

(def build-post-url (partial http/build-url "https://jsonplaceholder.typicode.com" "/posts"))

(defn get-by-id [id]
  (conv/to-model
   (http/exec-get (build-post-url id))))

(defn create [post]
  (conv/to-model
   (http/exec-post (build-post-url) (conv/from-model post))))

(defn update-post [post]
  (conv/to-model
   (http/exec-put (build-post-url (:id post)) (conv/from-model post))))

(defn delete [id]
  (http/exec-delete
   (build-post-url id)))

