(ns ring.middleware.okta
  "Ring middleware for Okta Single Sign-on"
  (:require [clojure.java.io :as io]
            [ring.util.response :as ring-response]
            [compojure.core :refer [POST defroutes]]
            [ring.ring-okta.session :as okta-session]
            [ring.ring-okta.predicates :as p]))

(defroutes okta-routes
  "The compojure routes for Okta

   POST /login
   POST /logout"

  {:added "0.1.0"}

  (POST "/login" request
        (okta-session/login request))

  (POST "/logout" request
        (okta-session/logout request)
        (merge (ring-response/redirect-after-post (:redirect-after-logout request)))))

(defn wrap-okta
  "Ring middleware for Okta Single Sign-on

  Accepts the following options:

  :okta-home             - (required) the URL to be redirected to for Okta login
                           e.g. https://company.okta.com

  :okta-config           - the location of the Okta configuration file
                           (defaults to \"resources/okta-config.xml\")

  :redirect-after-logout - the destination URL to be redirected to after a `POST /logout`
                           (defaults to \"/\")

  :skip-routes           - a list of routes to skip Okta authentication
                           e.g. [:get \"/about\" :get \"/contact\"]

  :force-user            - a default user to be used for development"

  {:arglists '([handler options]) :added "0.1.0"}
  [handler options]
  (fn [request]
    (if (nil? (:okta-home options))
      (throw (IllegalArgumentException. ":okta-home is required"))
      (cond
       (p/login? request) (handler (assoc request :okta-config-location (or (:okta-config options)
                                                                            (io/resource "okta-config.xml"))))
       (p/logout? request) (handler (assoc request :redirect-after-logout (or (:redirect-after-logout options)
                                                                              "/")))
       (p/logged-in? request) (handler request)
       (p/skip-route? request (:skip-routes options)) (handler request)
       (p/force-user? options) (handler (assoc-in request [:session :okta/user] (:force-user options)))
       :else (ring-response/redirect (:okta-home options))))))
