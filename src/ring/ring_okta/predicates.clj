(ns ring.ring-okta.predicates
  (:require [ring.util.request :as ring-request]))

(def not-nil? (comp not nil?))

(defn login? [{:keys [request-method] :as request}]
  (and (= request-method :post)
       (= (ring-request/path-info request) "/login")))

(defn logout? [{:keys [request-method] :as request}]
  (and (= request-method :post)
       (= (ring-request/path-info request) "/logout")))

(defn logged-in? [{:keys [session]}]
  (:okta/user session))

(defn force-user? [options]
  (not-nil? (:force-user options)))
