(ns ring.ring-okta.session-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojure.java.io :as io]
            [ring.ring-okta.session :refer [login]]))

(defn- stub-respond-to-okta-post [params okta-config]
  {:redirect-url "http://foo.bar.com"
   :authenticated-user-email "foo@bar.com"})

(defn- stub-redirect-after-post [url]
  {})

(deftest test-login
  (testing "user placed in session"
    (let [request {:params {}
                   :okta-config-location (io/resource "okta-config.xml")}]
      (with-redefs [ring.ring-okta.session/respond-to-okta-post stub-respond-to-okta-post
                    ring.util.response/redirect-after-post stub-redirect-after-post]
        (is (= "foo@bar.com" (-> (login request) :session :okta/user))))))

  (testing "redirect after login"
    (let [request {:params {}
                   :okta-config-location (io/resource "okta-config.xml")}]
      (with-redefs [ring.ring-okta.session/respond-to-okta-post stub-respond-to-okta-post]
        (is (= 303 (-> (login request) :status)))
        (is (= "http://foo.bar.com" (-> (login request) :headers (get "Location"))))))))
