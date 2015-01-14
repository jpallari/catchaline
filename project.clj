(defproject catchaline "0.1.0-SNAPSHOT"
  :description "TCP server for catching arbitrary lines of text"
  :url "http://github.com/jkpl/catchaline"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [aleph "0.4.0-alpha9"]
                 [byte-streams "0.2.0-alpha3"]
                 [clj-time "0.9.0"]
                 [guns.cli/optparse "1.1.2"]]
  :main catchaline.core)
