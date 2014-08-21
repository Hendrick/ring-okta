(defproject ring/ring-okta "0.1.0-SNAPSHOT"
  :description "Ring middleware for Okta Single Sign-on"
  :url "https://github.com/testdouble/ring-okta"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring/ring-core "1.3.0"]]
  :profiles {:dev {:dependencies [[ring-mock "0.1.5"]]}
             :1.3 {:dependencies [[org.clojure/clojure "1.3.0"]]}
             :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]} 
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}})
