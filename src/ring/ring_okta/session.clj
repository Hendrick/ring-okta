(ns ring.ring-okta.session
  (:require [ring.ring-okta.saml :as saml]
            [clojure.core.incubator :refer [dissoc-in]]
            [ring.util.response :as response]))

(defn login [{:keys [params okta-config-location]}]
  (let [okta-response (saml/respond-to-okta-post params okta-config-location)]
    (assoc-in
     (response/redirect-after-post (:redirect-url okta-response))
     [:session :okta/user]
     (:authenticated-user-email okta-response))))

(defn logout [request]
  (dissoc-in request [:session :okta/user]))
