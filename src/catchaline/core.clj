(ns catchaline.core
  (:require [aleph.tcp :as tcp]
            [manifold.stream :as s]
            [byte-streams :as bs]
            [guns.cli.optparse :as optparse]
            [clj-time [core :as t] [format :as tf]])
  (:import [java.io File])
  (:gen-class :main true))

(def time-formatter (tf/formatters :mysql))

(defn current-time [] (tf/unparse time-formatter (t/now)))

(defn with-current-time [s] (str (current-time) ": " s))

(def cli-options
  [["-p" "--port NUMBER" "Listen port"
    :default 9999
    :parse-fn #(Integer/parseInt %)]
   ["-h" "--host HOST" "Bind to host"
    :default "localhost"]
   ["-o" "--output FILE" "Output file"
    :default "catchaline.txt"]
   [nil "--help" "Print this help"]])

(defn start-pipe-server [output opts]
  (tcp/start-server
      (fn [client _] (s/connect client output))
      opts))

(defn print-to-outputs [s outs]
  (doseq [out outs]
    (bs/transfer s out {:close? false})))

(defn start-server [opts]
  (let [from-clients (s/stream)
        output-file (File. (:output opts))
        outputs [System/out output-file]]
     (->> from-clients
          (s/map bs/to-string)
          (s/map with-current-time)
          (s/consume #(print-to-outputs % outputs)))
     (start-pipe-server from-clients (select-keys opts [:host :port]))))

(defn- print-ready [opts]
  (println (str "Ready to catch lines at " (:host opts) ":" (:port opts))))

(defn -main [& args]
  (let [[opts _ help] (optparse/parse args cli-options)]
    (if (:help opts)
      (println help)
      (do (start-server opts)
          (print-ready opts)))))

