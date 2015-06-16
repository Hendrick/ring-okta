(ns ring.ring-okta.saml
  (:require [clojure.data.codec.base64 :as b64])
  (:import (com.okta.saml SAMLValidator)))

(defn get-valid-user-id [okta-config saml-response]
  (let [validator (SAMLValidator.)
        config (.getConfiguration validator okta-config)
        decoded-saml-response (String. (b64/decode (.getBytes saml-response "UTF-8")))
        valid-saml-response (.getSAMLResponse validator decoded-saml-response config)]
    (.getUserID valid-saml-response)))
