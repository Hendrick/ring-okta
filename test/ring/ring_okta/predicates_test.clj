(ns ring.ring-okta.predicates-test
  (:require [clojure.test :refer [deftest testing is]]
            [ring.ring-okta.predicates :as p]
            [ring.mock.request :refer [request]]))

(defmacro is-not
  ([form] `(is-not ~form nil))
  ([form msg] `(is (not ~form) ~msg)))

(deftest test-login?
  (testing "match method and route"
    (is (p/login? (request :post "/login"))))

  (testing "match method but not route"
    (is-not (p/login? (request :post "/foo"))))

  (testing "match route but not method"
    (is-not (p/login? (request :get "/login")))))

(deftest test-logout?
  (testing "match method and route"
    (is (p/logout? (request :post "/logout"))))

  (testing "match method but not route"
    (is-not (p/logout? (request :post "/foo"))))

  (testing "match route but not method"
    (is-not (p/logout? (request :get "/logout")))))

(deftest test-skip-route?
  (is (p/skip-route? (request :get "/foo")
                     [:get "/foo" :post "/foo"]))
  (is-not (p/skip-route? (request :get "/foo")
                         [:get "/bar"]))
  (is-not (p/skip-route? (request :get "/foo")
                         [:post "/foo"]))
  (is-not (p/skip-route? (request :get "/foo")
                         nil))

  (testing ":any can be used to match all route methods"
    (is (p/skip-route? (request :get "/foo")
                       [:any "/foo"]))
    (is (p/skip-route? (request :post "/foo")
                       [:any "/foo"]))))
