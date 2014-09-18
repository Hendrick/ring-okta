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

(defn skip-route? [{:keys [request-method] :as request} skip-routes]
  (when skip-routes
    (let [route-index (.indexOf skip-routes (ring-request/path-info request))
          route-method (get skip-routes (dec route-index))]
      (and (> route-index -1)
           (or (= :any route-method)
               (= request-method route-method))))))

(defn force-user? [force-user]
  (not-nil? force-user))
