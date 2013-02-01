(ns scripper.handler
  (:use compojure.core scripper.view
         [ring.middleware.format-params :only [wrap-json-params]]
         [ring.middleware.format-response :only [wrap-json-response]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [scripper.core :as core]
            [ring.adapter.jetty :as ring]))
  (use 'hiccup.bootstrap.middleware 'ring.util.response)

(defroutes app-routes
  (GET "/" [] (url-form))
  ; (GET "/search/" [url] (response (core/mp3-json url)))
  (route/resources "/")
  (GET "/:sc-user" [sc-user]
       (response (core/mp3-json
                   (str "https://soundcloud.com/" sc-user))))
  (GET "/:sc-user/:sc-track" [sc-user sc-track]
       (response (core/mp3-json
                   (str "https://soundcloud.com/" sc-user "/" sc-track))))
  (GET "/:sc-user/sets/:sc-track" [sc-user sc-track]
       (response (core/mp3-json
                   (str "https://soundcloud.com/" sc-user "/sets/" sc-track))))
  (route/not-found "Not Found"))

(def app
  (->
   (handler/site app-routes)
   (wrap-bootstrap-resources)
   (wrap-json-response)))

(defn start [port]
  (ring/run-jetty app {:port port :join? false}))

(defn -main []
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (start port)))
