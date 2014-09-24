(ns clojure.test.helpers
  (:require [clojure.test :refer [is]]))

(defmacro is-not
  ([form] `(is-not ~form nil))
  ([form msg] `(is (not ~form) ~msg)))
