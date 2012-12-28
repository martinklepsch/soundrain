(ns scripper.handler
  (:use compojure.core scripper.view)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))
  (use 'hiccup.bootstrap.middleware)

(defroutes app-routes
  (GET "/" [] (url-form))
  (POST "/" [url] (results url))
  (route/not-found "Not Found"))

(def app
  (wrap-bootstrap-resources( handler/site app-routes)))
