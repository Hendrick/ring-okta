(ns ring.ring-okta.session-test
  (:require [clojure.test :refer [deftest testing is]]
            [ring.ring-okta.session :refer [login logout]]))

(defn- stub-respond-to-okta-post [& args]
  {:redirect-url "http://foo.bar.com"
   :authenticated-user-email "foo@bar.com"})

(defn- stub-redirect-after-post [& args]
  {})

(deftest test-login
  (let [request {:params {} :okta-config-location "foo.xml"}]
    (with-redefs [ring.ring-okta.session/respond-to-okta-post stub-respond-to-okta-post]
      (testing "user placed in session"
        (is (= "foo@bar.com" (-> (login request) :session :okta/user))))

      (testing "redirect after login"
        (is (= 303 (-> (login request) :status)))
        (is (= "http://foo.bar.com" (-> (login request) :headers (get "Location"))))))))

(deftest test-logout
  (let [request {:params {:foo "foo"}
                 :session {:okta/user "foo@bar.com"
                           :bar "bar"}}]
    (testing "logout removes only :okta/user from session"
      (is (= {:bar "bar"} (-> (logout request) :session))))

    (testing "logout does not clear other items in the request"
      (is (= {:params {:foo "foo"} :session {:bar "bar"}} (logout request))))))
