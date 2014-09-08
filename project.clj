(defproject ring/ring-okta "0.1.0-SNAPSHOT"
  :description "Ring middleware for Okta Single Sign-on"
  :url "https://github.com/Hendrick/ring-okta"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"local" ~(str (.toURI (java.io.File. "maven_repository")))}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring/ring-core "1.3.1"]
                 [compojure "1.1.9"]
                 [org.clojure/data.codec "0.1.0"]
                 [com.okta/saml-toolkit "1.0.5-000148-30924be" :exclusions [[org.slf4j/slf4j-api]]]

                 ;; okta dependencies -- some are not specified in their pom,
                 ;; others are borked because of our weird local repo thing that
                 ;; we do in order to please the Travis-CI gods
                 [com.sun.xml.parsers/jaxp-ri "1.4.5"]
                 [com.google.inject/guice "3.0"]
                 [org.bouncycastle/bcprov-jdk16 "1.45"]
                 [org.apache.commons/commons-lang3 "3.0"]
                 [javax.servlet/javax.servlet-api "3.0.1" :scope "provided"]
                 [org.opensaml/opensaml "2.5.1-1"]]
  :plugins [[codox "0.8.10"]]
  :codox {:include ring.middleware.okta}
  :profiles {:dev {:dependencies [[ring-mock "0.1.5"]]}
             :1.3 {:dependencies [[org.clojure/clojure "1.3.0"]]}
             :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]} 
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}})
