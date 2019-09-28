(ns net.vemv.idealistic.api
  (:require
   [net.vemv.idealistic.impl.auth :refer [get-access-token!]]
   [net.vemv.idealistic.impl.queries :refer [queries]]
   [net.vemv.idealistic.impl.search :refer [search!]]
   [net.vemv.idealistic.impl.train-stations :as train-stations]))

(defn -main [& _]
  (let [api-hits (atom 0)
        known-results (atom #{})]
    (doseq [{:keys [tier] :as kind} @queries
            :let [_ (println "Searching, tier" tier "-----------------------")]
            {:keys         [center]
             location-name :name
             :minutes/keys [train-distance-to-barcelona]} (train-stations/all)]
      (println (str "Searching in " location-name " (" train-distance-to-barcelona " minutes away in train from Barcelona)"))
      (let [access-token (get-access-token!)
            overrides (assoc kind :center center)
            filters {}
            removals {}]
        (search! {:access-token  access-token
                  :overrides     overrides
                  :known-results known-results
                  :filters       filters
                  :removals      removals})
        (swap! api-hits inc)))
    (println "Hit the API" @api-hits "times.")))
