(defproject scripper "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [clj-http "0.6.2"]
                 [org/jaudiotagger "2.0.3"]
                 [enlive "1.0.0-SNAPSHOT"]
                 [commons-lang "2.5"]
                 [compojure "1.1.3"]
                 [ccfontes/hiccup-bootstrap "0.1.3"]]
  :plugins [[lein-ring "0.7.5"]]
  :ring {:handler scripper.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}}
  :main scripper.core)