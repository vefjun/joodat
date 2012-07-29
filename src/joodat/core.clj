(ns joodat.core
  (:use
    [compojure.core :only (defroutes GET)]
    [compojure.route :only (not-found)]
    [joodo.middleware.view-context :only (wrap-view-context)]
    [joodo.views :only (render-template render-html)]
    [joodo.controllers :only (controller-router)]))

(defroutes joodat-routes
  (GET "/" [] (render-template "index"))
  (controller-router 'joodat.controller)
  (not-found (render-template "not_found" :template-root "joodat/view" :ns `joodat.view.view-helpers)))

(def app-handler
  (->
    joodat-routes
    (wrap-view-context :template-root "joodat/view" :ns `joodat.view.view-helpers)))

