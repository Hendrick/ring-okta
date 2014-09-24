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
          (is (= (-> response :body :okta-config-location) custom-okta-config))))
      (testing "user in session")
      (testing "redirected after login")))

  (testing "#logout"
    (testing "user not in session")
    (testing "redirected after logout"
      (testing "defined")
      (testing ":okta-home")
      (testing "default")))

  (testing "#logged-in")
  (testing "#skip-routes"
    (testing "skip a defined request method")
    (testing "skip any request method"))
  (testing "#force-user")
  (testing "#other"))
