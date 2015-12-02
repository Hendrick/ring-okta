(defproject ring-okta "0.1.6"
  :description "Ring middleware for Okta Single Sign-on"
  :url "https://github.com/Hendrick/ring-okta"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"local" ~(str (.toURI (java.io.File. "maven_repository")))}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.incubator "0.1.3"]
                 [ring/ring-core "1.4.0"]
                 [ring-mock "0.1.5" :scope "test"]
                 [compojure "1.4.0"]
                 [org.clojure/data.codec "0.1.0"]
                 [com.okta/saml-toolkit "1.0.12-000170-c7ed721"]

                 ;; okta dependencies -- some are not specified in their pom,
                 ;; others are borked because of our weird local repo thing that
                 ;; we do in order to please the Travis-CI gods
                 [com.sun.xml.parsers/jaxp-ri "1.4.5"]
                 [org.slf4j/slf4j-api "1.6.1" :scope "provided"]
                 [org.slf4j/slf4j-simple "1.6.1" :scope "test"]
                 [com.google.inject/guice "3.0"]
                 [org.bouncycastle/bcprov-jdk16 "1.45"]
                 [org.apache.commons/commons-lang3 "3.0"]
                 [javax.servlet/javax.servlet-api "3.0.1" :scope "provided"]
                 [org.opensaml/opensaml "2.6.4"]]
  :plugins [[codox "0.8.12"]
            [lein-cloverage "1.0.6"]]
  :codox {:include ring.middleware.okta
          :output-dir "../ring-okta-doc"}
  :profiles {:dev {:resource-paths ["test-resources"]}
             :1.5 {:resource-paths ["test-resources"]
                   :dependencies [[org.clojure/clojure "1.5.1"]]}
             :1.6 {:resource-paths ["test-resources"]
                   :dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7 {:resource-paths ["test-resources"]
                   :dependencies [[org.clojure/clojure "1.7.0"]]}}
  :aliases {"test-all-profiles" ["with-profile" "dev:1.5:1.6:1.7" "test"]
            "cloverage" ["do" "cloverage" "--output" "doc/coverage"]
            "release" ["do" "clean," "deploy" "clojars"]})
