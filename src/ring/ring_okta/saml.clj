(ns ring.ring-okta.saml
  (:require [clojure.data.codec.base64 :as b64]
            [clojure.java.io :as io]
            [clojure.string :as string])
  (:import (com.okta.saml SAMLValidator)))

(defn- get-valid-user-id [okta-config saml-response]
  (let [validator (SAMLValidator.)
        config (.getConfiguration validator okta-config)
        decoded-saml-response (String. (b64/decode (.getBytes saml-response "UTF-8")))
        valid-saml-response (.getSAMLResponse validator decoded-saml-response config)]
    (.getUserID valid-saml-response)))

(defn respond-to-okta-post [params okta-config-location]
  (let [okta-config (slurp (io/reader okta-config-location))]
    {:redirect-url (:RelayState params)
     :authenticated-user-email (string/lower-case
                                (get-valid-user-id okta-config (:SAMLResponse params)))}))
