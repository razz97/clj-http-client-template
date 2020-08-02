(ns http-client-template.modules.http-client
  (:require [clj-http.lite.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [http-client-template.modules.logger :as logger]
            [config.core :refer [env]]))

;; Creating logger function and pulling config for this ns

(def log (logger/logf-for-context "HTTP-CLIENT"))

(def config (:http env))
(def default-retries (:default-retries config))

;; Functions for building request result messages

(defn build-result-msg [ok? req resp]
  (str "Request " (if ok? "successful" "failure") ": " (:method req) "@" (:url req) ", "
       (if-let [status (:status resp)]
         (str "status code - " status)
         (str "error - " (:error resp)))))

(def build-ok-msg (partial build-result-msg true))
(def build-err-msg (partial build-result-msg false))

;; Functions for executing requests

(defn try-exec [req]
  (try (let [resp (client/request req) status (:status resp)]
         (if (and (>= status 200) (< status 300))
           (when-let [parsed (json/read-str (:body resp))]
             (log :info (build-ok-msg req resp)) parsed)
           (let [err (build-err-msg req resp)]
             (log :err err) {:error err})))
       (catch Exception e
         (let [err {:error (.getMessage e)}]
           (log :err (build-err-msg req err)) err))))

(defn try-exec-times [req n]
  (loop [i n]
    (let [res (try-exec req) success? (not (:error res))]
      (if (or (zero? i) success?)
        res
        (recur (dec i))))))

;; Functions for building requests

(defn req-base [url method] {:url url :method method})

(defn req-with-body [url method body]
  (assoc (req-base url method) :body (json/write-str body)))

(defn req-with-params [url method params]
  (assoc (req-base url method) :query-params params))

;; This should be the interface

(defn build-url [url & paths] (str url "/" (string/join "/" paths)))

(defn exec-get
  ([url params retries] (try-exec-times (req-with-params url :get params) retries))
  ([url params] (exec-get url params default-retries))
  ([url] (exec-get url {})))

(defn exec-post
  ([url body retries] (try-exec-times (req-with-body url :post body) retries))
  ([url body] (exec-post url body default-retries))
  ([url] (exec-post url {})))

(defn exec-put
  ([url body retries] (try-exec-times (req-with-body url :put body) retries))
  ([url body] (exec-put url body default-retries))
  ([url] (exec-put url {})))

(defn exec-delete
  ([url retries] (try-exec-times (req-base url :delete) retries))
  ([url] (exec-delete url default-retries)))
