(ns http-client-template.datasource.json-placeholder.converter)

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