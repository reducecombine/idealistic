(ns net.vemv.idealistic.impl.train-stations
  (:require
   [clojure.spec.alpha :as spec]
   [clojure.string :as string]
   [nedap.speced.def :as speced]
   [nedap.utils.spec.predicates :refer [pos-integer? present-string?]]))

(def sabadell
  "Follows the S2 line (Rodalies sucks)."
  [{:center                              "41.542653,2.100874"
    :name                                "Sabadell Can Feu"
    :minutes/train-distance-to-barcelona 40} ;; XXX sabadell train dists are made up
   {:center                              "41.547312,2.108645"
    :name                                "Sabadell Plaça Major"
    :minutes/train-distance-to-barcelona 43}
   {:center                              "41.555755,2.102133"
    :name                                "La Creu Alta"
    :minutes/train-distance-to-barcelona 46}
   {:center                              "41.561948,2.096239"
    :name                                "Sabadell Nord"
    :minutes/train-distance-to-barcelona 49}
   {:center                              "41.571296,2.089570"
    :name                                "Sabadell Parc del Nord"
    :minutes/train-distance-to-barcelona 52}])

(def south-coast
  [{:center                              "41.220403,1.730695"
    :name                                "Vilanova"
    :minutes/train-distance-to-barcelona 45}
   {:center                              "41.204312,1.675979"
    :name                                "Cubelles"
    :minutes/train-distance-to-barcelona 47}
   {:center                              "41.195022,1.631921"
    :name                                "Cunit"
    :minutes/train-distance-to-barcelona 51}])

(def north-coast
  [{:center                              "41.445890,2.248929"
    :name                                "Badalona"
    :minutes/train-distance-to-barcelona 20}
   {:center                              "41.463028,2.272115"
    :name                                "Montgat"
    :minutes/train-distance-to-barcelona 30}
   {:center                              "41.468779,2.286670"
    :name                                "Montgat Nord"
    :minutes/train-distance-to-barcelona 30}
   {:center                              "41.477172,2.310690"
    :name                                "Masnou"
    :minutes/train-distance-to-barcelona 42}
   {:center                              "41.487698,2.354832"
    :name                                "Premia de Mar"
    :minutes/train-distance-to-barcelona 45}
   {:center                              "41.500441,2.389473"
    :name                                "Vilassar de Mar"
    :minutes/train-distance-to-barcelona 47}
   {:center                              "41.533377,2.445439"
    :name                                "Mataro"
    :minutes/train-distance-to-barcelona 50}
   {:center                              "41.568539,2.526031"
    :name                                "Caldetas"
    :minutes/train-distance-to-barcelona 54}])

(spec/def ::center (spec/and present-string?
                             (fn [s]
                               (string/includes? s ","))
                             (fn [s]
                               (not (string/includes? s " ")))))

(spec/def ::name present-string?)

(spec/def :minutes/train-distance-to-barcelona pos-integer?)

(spec/def ::train-station (spec/keys :req    [:minutes/train-distance-to-barcelona]
                                     :req-un [::center
                                              ::name]))

(speced/defn ^{::speced/spec (spec/coll-of ::train-station)} all []
  (reduce into [south-coast north-coast sabadell]))
