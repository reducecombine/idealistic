(ns net.vemv.idealistic.impl.queries
  (:require
   [clojure.java.io :as io]))

(def queries
  (delay
    (-> "queries.edn" io/resource slurp read-string)))
