(ns ring.ring-okta.session
  (:require [clojure.data.codec.base64 :as b64]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [ring.util.response :as ring-response])
  (:import (com.okta.saml SAMLValidator)))

(defn- respond-to-okta-post [params okta-config]
  (let [validator (SAMLValidator.)
        config (.getConfiguration validator okta-config)
        encoded-saml-response (String. (b64/decode (.getBytes (:SAMLResponse params) "UTF-8")))
        saml-response (.getSAMLResponse validator encoded-saml-response config)
        authenticated-user-email (.getUserID saml-response)
        redirect-url (:RelayState params)]
    {:redirect-url redirect-url
     :authenticated-user-email (string/lower-case authenticated-user-email)}))

(defn login [{:keys [params okta-config-location]}]
  (let [okta-config (slurp (io/reader okta-config-location))
        okta-response (respond-to-okta-post params okta-config)]
    (-> (ring-response/redirect (:redirect-url okta-response))
        (assoc-in [:session :okta/user] (:authenticated-user-email okta-response)))))

(defn logout [{:keys [session]}]
  (dissoc session :okta/user))
