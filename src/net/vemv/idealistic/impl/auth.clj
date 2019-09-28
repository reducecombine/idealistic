(ns net.vemv.idealistic.impl.auth
  (:require
   [cheshire.core :as cheshire]
   [clj-http.client :as http.client]
   [clj-http.util :refer [base64-encode url-encode]]
   [clojure.spec.alpha :as spec]
   [nedap.speced.def :as speced]
   [nedap.utils.spec.predicates :refer [present-string?]]
   [net.vemv.idealistic.impl.queries :refer [queries]]
   [net.vemv.idealistic.impl.train-stations :as train-stations])
  (:import
   (java.io File)))

(def cache-filename ".idealistic-credentials")

(defn credentials []
  (speced/let [^present-string? api-key (System/getenv "IDEALISTIC_API_KEY")
               ^present-string? api-secret (System/getenv "IDEALISTIC_API_SECRET")]
    (->> [api-key api-secret]
         (map url-encode)
         (interpose ":")
         ^String (apply str)
         (.getBytes)
         (base64-encode)
         (str "Basic "))))

(def content-type "application/x-www-form-urlencoded;charset=UTF-8")

(spec/def ::access-token present-string?)

(speced/defn ^::access-token perform-auth-request! []
  (speced/let [{:keys [^{::speced/spec #{200}} status
                       body]} (-> "https://api.idealista.com/oauth/token"
                                  (http.client/post {:headers     {"Authorization" (credentials)
                                                                   "Content-Type"  content-type}
                                                     :form-params {:grant_type "client_credentials"}}))

               {:keys [^::access-token access_token
                       ^nat-int? expires_in]
                :as   body}
               (cheshire/parse-string body keyword)]
    (spit cache-filename body)
    access_token))

(speced/defn ^::access-token get-access-token! []
  (if-not (-> ^String cache-filename File. .exists)
    (perform-auth-request!)
    (speced/let [{:keys [^::access-token access_token, ^nat-int? expires_in]} (-> cache-filename slurp read-string)
                 criterion (* (count (train-stations/all))
                              (count @queries)
                              ;; throttle time + response time:
                              2)]
      (if (> expires_in criterion) ;; seconds
        access_token
        (perform-auth-request!)))))
