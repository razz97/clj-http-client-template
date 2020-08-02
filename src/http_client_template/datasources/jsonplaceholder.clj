(ns http-client-template.datasources.jsonplaceholder
  (:require [http-client-template.modules.http-client :as http]))

(def build-post-url (partial http/build-url "https://jsonplaceholder.typicode.com" "posts"))

;; Convert functions 

(defn to-model [dct]
  (if (:error dct) dct
      {:id      (dct "id")
       :user-id (dct "userId")
       :title   (dct "title")
       :body    (dct "body")}))

(defn from-model [dct]
  {"id"     (:id dct)
   "userId" (:user-id dct)
   "title"  (:title dct)
   "body"   (:body dct)})

;; This should be the interface

(defn get-post-by-id [id]
  (to-model
   (http/exec-get (build-post-url id))))

(defn create-post [post]
  (to-model
   (http/exec-post (build-post-url) (from-model post))))

(defn update-post [post]
  (to-model
   (http/exec-put (build-post-url (:id post)) (from-model post))))

(defn delete-post [id]
  (http/exec-delete (build-post-url id)))

