(ns scripper.core
  (:require [clojure.java.io :as io]
            [scripper.metadata :as metadata]
            [scripper.parse :as parse]))



 (defn download-mp3 [tags]
   "downloads the mp3 and sets the tags"
   (let [artist (:username (:user tags))
         title 	(:title tags)
         album 	(:username (:user tags))
         year 	"2012"
         mp3 		(:streamUrl tags)
         image 	(:image tags)]
     (metadata/tag-mp3 {:artist artist
                        :mp3 mp3
                        :title title
                        :album album
                        :year year
                        :image image})
     (str "music/" artist " - " title ".mp3")))

 (defn download-mp3s [url]
   "calls download-mp3 for every song found on the page"
   (map download-mp3 (parse/get-metainformations url)))

(defn -main
  "I don't do a whole lot."
  [& args]
  (str "blah"))

