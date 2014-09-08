(ns ring.middleware.okta-test
  (:require [clojure.test :refer [deftest testing is]]
            [ring.middleware.okta :refer [wrap-okta]]
            [ring.mock.request :refer [request]]
            [ring.util.response :refer [response]]))

(deftest test-wrap-okta
  (testing ":okta-home option is required"
    (let [default-handler #(response %)]
      (let [handler (wrap-okta default-handler)]
        (is (thrown? IllegalArgumentException
                     (handler (request :get "/"))))))))
