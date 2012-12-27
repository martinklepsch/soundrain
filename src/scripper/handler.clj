(ns scripper.handler
  (:use compojure.core scripper.view)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] (url-form))
  (POST "/" [url] (results url))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
