(ns scripper.handler
  (:use compojure.core scripper.view
         [ring.middleware.format-params :only [wrap-json-params]]
        		[ring.middleware.format-response :only [wrap-json-response]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [scripper.core :as core]
           ))
  (use 'hiccup.bootstrap.middleware 'ring.util.response)

(defroutes app-routes
  (GET "/" [] (url-form))
  (GET "/search/" [url] (response (core/mp3-json url)))
  (GET "/:sc-user/:sc-track" [sc-user sc-track]
       (response (core/mp3-json
                   (str "https://soundcloud.com/" sc-user "/" sc-track))))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (->  
   (handler/site app-routes)
   (wrap-bootstrap-resources)
   (wrap-json-response)))
