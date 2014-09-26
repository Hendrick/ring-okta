(ns ring.ring-okta.session-test
  (:require [clojure.test :refer [deftest testing is]]
            [ring.ring-okta.session :refer [login logout]]))

(defn- stub-respond-to-okta-post [& args]
  {:redirect-url "http://foo.bar.com"
   :authenticated-user-email "foo@bar.com"})

(defn- stub-redirect-after-post [& args]
  {})

(deftest test-login
  (testing "user placed in session"
    (let [request {:params {}
                   :okta-config-location "foo.xml"}]
      (with-redefs [ring.ring-okta.session/respond-to-okta-post stub-respond-to-okta-post]
        (is (= "foo@bar.com" (-> (login request) :session :okta/user))))))

  (testing "redirect after login"
    (let [request {:params {}
                   :okta-config-location "foo.xml"}]
      (with-redefs [ring.ring-okta.session/respond-to-okta-post stub-respond-to-okta-post]
        (is (= 303 (-> (login request) :status)))
        (is (= "http://foo.bar.com" (-> (login request) :headers (get "Location"))))))))

(deftest test-logout
  (testing "logout removes user from session"
    (let [request {:params {:foo "foo"}
                   :session {:okta/user "foo@bar.com"
                             :bar "bar"}}
          logout-response (logout request)]
      (is (nil? (-> logout-response :session :okta/user)))
      (is (= {:params {:foo "foo"} :session {:bar "bar"}} logout-response)))))
