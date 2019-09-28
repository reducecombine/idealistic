(ns net.vemv.idealistic.impl.search
  (:require
   [cheshire.core :as cheshire]
   [clj-http.client :as http.client]
   [clojure.spec.alpha :as spec]
   [nedap.speced.def :as speced]
   [nedap.utils.spec.predicates :refer [present-string?]]))

(spec/def ::overrides (spec/and (spec/keys :req-un [::center])
                                (fn [{:keys [flat chalet]}]
                                  (if flat
                                    (not chalet)
                                    chalet))))

;; XXX there's no "plantas intermedidas" filter? (for flats)
;; XXX there's no desc filter? (would serve to filter 'esquineros')

(speced/defn search-uri [^::overrides overrides]
  (let [{:keys [flat chalet] :as query} (-> {:country          "es"
                                             :maxItems         50
                                             :locale           "es"
                                             :operation        "rent"
                                             :propertyType     "homes"
                                             :numPage          1
                                             :sinceDate        "M"
                                             :order            "price"
                                             :sort             "asc"
                                             :hasMultimedia    true
                                             :minSize          90.0
                                             :bedrooms         "2,3,4"
                                             :preservation     "good"
                                             :furnished        "furnishedKitchen"
                                             :airConditioning  true
                                             :builtinWardrobes true}
                                            (merge overrides)
                                            clj-http.client/generate-query-string)]
    (str "https://api.idealista.com/3.5/es/search?" query)))

(speced/defn search! [{:keys                                              [access-token
                                                                           overrides
                                                                           filters
                                                                           removals
                                                                           known-results]
                       {:flats/keys [^boolean? skip-duplex-requirement?]} :overrides}]
  (Thread/sleep 1000) ;; ensure rate limit (for now)
  (println (str "Results for "
                (pr-str {:overrides overrides, :filters filters, :removals removals})
                ":"))
  (-> (with-out-str
        (cond->> (-> overrides
                     search-uri
                     (http.client/post {:headers {"authorization"
                                                  (str "Bearer " access-token)}})
                     (:body)
                     (cheshire/parse-string keyword)
                     (:elementList))
          true                                 (map (speced/fn [{:keys [^present-string? url] :as element}]
                                                      (swap! known-results conj url)
                                                      element))
          (->> overrides keys (some #{:flat})) (filter (speced/fn [{{:keys [^present-string? subTypology]} :detailedType}]
                                                         (if skip-duplex-requirement?
                                                           true
                                                           (#{"duplex"} subTypology))))
          true                                 (remove (speced/fn [{:keys [^present-string? url]}]
                                                         (@known-results url)))
          true                                 (map (speced/fn [{:keys [^present-string? url]}]
                                                      {:url url}))
          true                                 (clojure.pprint/pprint)))
      println))
