(ns ring.middleware.okta
  "Ring middleware for Okta Single Sign-on"
  (:require [clojure.java.io :as io]
            [ring.util.response :as ring-response]
            [ring.util.request :as ring-request]
            [compojure.core :refer [POST defroutes]]
            [ring.ring-okta.session :as okta-session]))

(defn- login? [{:keys [request-method] :as request}]
  (and (= request-method :post)
       (= (ring-request/path-info request) "/login")))

(defn- logout? [{:keys [request-method] :as request}]
  (and (= request-method :post)
       (= (ring-request/path-info request) "/logout")))

(defn- logged-in? [{:keys [session]}]
  (:okta/user session))

(defn- force-user? [options]
  (:force-user options))

(defroutes okta-routes
  "The compojure routes for Okta

   POST /login
   POST /logout"

  {:added "0.1.0"}

  (POST "/login" request
       (okta-session/login request))
  (POST "/logout" request
       (okta-session/logout request)
       (merge (ring-response/redirect (:redirect-after-logout request)))))

(defn wrap-okta
  "Ring middleware for Okta Single Sign-on

  Accepts the following options:

  :okta-home             - (required) the URL to be redirected to for Okta login
                           e.g. https://company.okta.com

  :okta-config           - the location of the Okta configuration file
                           (defaults to \"resources/okta-config.xml\")

  :redirect-after-logout - the destination URL to be redirected to after a `POST /logout`
                           (defaults to :okta-home then falls back to \"/\")

  :force-user            - a default user to be used for development"

  {:arglists '([handler] [handler options]) :added "0.1.0"}
  [handler & [{:as options}]]
  (fn [request]
    (cond
     (login? request) (handler (assoc request :okta-config-location (or (:okta-config options)
                                                                        (io/resource "okta-config.xml"))))
     (logout? request) (handler (assoc request :redirect-after-logout (or (:redirect-after-logout options)
                                                                          (:okta-home options)
                                                                          "/")))
     (logged-in? request) (handler request)
     (force-user? options) (handler (assoc-in request [:session :okta/user] (:force-user options)))
     :else (ring-response/redirect (:okta-home options)))))
