(ns http-client-template.modules.logger
  (:require [config.core :refer [env]]
            [clojure.string :as string]))

(import java.time.LocalDateTime
        java.time.format.DateTimeFormatter)

(def config (:logger env))

(def build-log
  (let [format-log (fn [msg & args] (str "[" (string/join "][" args) "] - " msg))]
    (if (:log-time? config)
      (let [formatter (DateTimeFormatter/ofPattern (:time-pattern config))]
        (fn [msg type context] (format-log msg (.format formatter (LocalDateTime/now)) context type)))
      format-log)))

(defn log [type context msg]
  (let [result (build-log msg type context)]
    (println result)
    (when (:save-logs? config)
      (spit (:output-file config) (str result "\n") :append true))))

;; This should be the interface

(def err (partial log "ERROR"))
(def info (partial log "INFO"))
(def debug (if (:log-debug? config) (partial log "DEBUG") #()))

(defn logf-for-context
  [context]  (fn [level msg]
               (case level
                 :err (err context msg)
                 :info (info context msg)
                 :debug (debug context msg))))

;; (defn logger [context level msg]
;;   (let [lvl (level {:err "ERROR"})]
;;     ( (build-dog lvl context msg)))

;; (def log (partial logger "HTTP"))

;; (log :err "asdfghjklñ")


