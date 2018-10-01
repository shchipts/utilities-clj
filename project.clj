(defproject org.iiasa/utilities-clj "1.1.0-SNAPSHOT"
  :description "Commonly used functions."
  :url "https://github.com/shchipts/utilities-clj"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.cli "0.4.1"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [org.clojure/java.classpath "0.3.0"]
                 [clojure-csv/clojure-csv "2.0.2"]
                 [com.cemerick/pomegranate "1.0.0"
                  :exclusions [org.slf4j/jcl-over-slf4j
                               org.codehaus.plexus/plexus-utils]]
                 [org.apache.httpcomponents/httpcore "4.4.10"]
                 [org.slf4j/slf4j-nop "1.7.25"]
                 [criterium "0.4.4"]]
  :plugins [[lein-codox "0.9.5"]]
  :codox {:output-path "docs"}
  :main nil
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
