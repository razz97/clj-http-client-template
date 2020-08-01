(ns http-client-template.datasource.client
  (:require [clj-http.lite.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as string]))

(defn status-ok? [status]
  (and (>= status 200) (< status 300)))

(defn build-url [url & paths] (str url (string/join "/" paths)))

(defn error [req resp]
  (let [msg (str "Request failure: " req ", response: " (dissoc resp :headers))]
    (prn msg) msg))

(defn success [req resp]
  (let [msg (str "Request successful: " req ", response: " (dissoc resp :headers))]
    (prn msg) msg))

(str {:req {:lala "po"}})

(defn try-once [req]
  (try (let [resp (client/request req)]
         (if (status-ok? (:status resp))
           (json/read (:body resp))
           (:error (error req resp))))
       (catch Exception e
         {:error (error req {:message (.getMessage e)})})))

(defn try-times [n req]
  (loop [i n]
    (let [response (try-once req)]
      (if (or (:error response) (zero? i))
        (recur (dec i))
        (do (success req response) response)))))

(defn exec-get
  ([url params] (try-times 3 {:url url :query-params params :method :get}))
  ([url] (exec-get url {})))

(defn exec-post
  ([url body] (try-times 3 {:url url :body (json/write-str body) :method :post}))
  ([url] exec-post url {}))

(defn exec-put [url body]
  (try-times 3 {:url url :body (json/write-str body) :method :put}))

(defn exec-delete [url]
  (try-times 3 {:url url :method :delete}))

