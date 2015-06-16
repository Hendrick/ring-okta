(ns ring.ring-okta.session
  (:require [ring.ring-okta.saml :as saml]
            [clojure.core.incubator :refer [dissoc-in]]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [ring.util.response :as response]))

(defn- respond-to-okta-post [params okta-config-location]
  (let [okta-config (slurp (io/reader okta-config-location))]
    {:redirect-url (:RelayState params)
     :authenticated-user-email (string/lower-case
                                (saml/get-valid-user-id okta-config (:SAMLResponse params)))}))

(defn login [{:keys [params okta-config-location]}]
  (let [okta-response (respond-to-okta-post params okta-config-location)]
    (assoc-in
     (response/redirect-after-post (:redirect-url okta-response))
     [:session :okta/user]
     (:authenticated-user-email okta-response))))

(defn logout [request]
  (dissoc-in request [:session :okta/user]))
