(ns ring.middleware.okta-test
  (:require [clojure.test :refer [deftest testing is]]
            [clojure.test.helpers :refer [is-not]]
            [clojure.java.io :as io]
            [ring.middleware.okta :refer [wrap-okta]]
            [ring.mock.request :refer [request]]
            [ring.util.response :refer [response]]))

(def okta-home "https://company.okta.com")
(def default-okta-config "okta-config.xml")
(def custom-okta-config "custom-okta-config.xml")

(deftest test-wrap-okta
  (let [default-handler #(response %)]

    (testing ":okta-home option is required"
      (testing "with :okta-home"
        (let [handler (wrap-okta default-handler {:okta-home okta-home})]
          (is-not (nil? (handler (request :get "/"))))))

      (testing "without :okta-home"
        (let [handler (wrap-okta default-handler {})]
          (is (thrown? IllegalArgumentException
                       (handler (request :get "/")))))))

    (testing "#login"
      (testing "with default :okta-config"
        (let [handler (wrap-okta default-handler {:okta-home okta-home})
              response (handler (request :post "/login"))]
          (is (= (-> response :body :okta-config-location .getPath)
                 (-> default-okta-config io/resource .getPath)))))

      (testing "with defined :okta-config"
        (let [handler (wrap-okta default-handler {:okta-home okta-home
                                                  :okta-config custom-okta-config})
              response (handler (request :post "/login"))]
          (is (= (-> response :body :okta-config-location) custom-okta-config)))))

    (testing "#logout"
      (testing "with default :redirect-after-logout"
        (let [handler (wrap-okta default-handler {:okta-home okta-home})
              response (handler (request :post "/logout"))]
          (is (= "/" (-> response :body :redirect-after-logout)))))

      (testing "with defined :redirect-after-logout"
        (let [handler (wrap-okta default-handler {:okta-home okta-home
                                                  :redirect-after-logout "/home"})
              response (handler (request :post "/logout"))]
          (is (= "/home" (-> response :body :redirect-after-logout)))))
      (testing ":okta-home"))

    (testing "#logged-in")

    (testing "#skip-routes"
      (testing "with :skip-routes defined"
        (let [handler (wrap-okta default-handler {:okta-home okta-home
                                                  :skip-routes [:get "/foo"]})
              response (handler (request :get "/foo"))]
          (is (= "/foo" (-> response :body :uri)))))

      (testing "without :skip-routes defined"
        (let [handler (wrap-okta default-handler {:okta-home okta-home})
              response (handler (request :get "/foo"))]
          (is (nil? (-> response :body :uri))))))

    (testing "#force-user"
      (testing "with :force-user defined"
        (let [handler (wrap-okta default-handler {:okta-home okta-home
                                                  :force-user "foo@bar.com"})
              response (handler (request :get "/foo"))]
          (is (= "foo@bar.com" (-> response :body :session :okta/user)))))

      (testing "without :force-user defined"
        (let [handler (wrap-okta default-handler {:okta-home okta-home})
              response (handler (request :get "/foo"))]
          (is (nil? (-> response :body :session :okta/user))))))

    (testing "non-authenticated request"
      (let [handler (wrap-okta default-handler {:okta-home okta-home})
            response (handler (request :get "/foo"))]
        (is (= 302 (-> response :status)))
        (is (= okta-home (-> response :headers (get "Location"))))))))
