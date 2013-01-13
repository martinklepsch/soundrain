(ns scripper.core
  (:require [clojure.java.io :as io]
            [scripper.metadata :as metadata]
            [scripper.parse :as parse]))



 (defn download-mp3 [tags]
   "downloads the mp3 and sets the tags"
   (let [{{artist :username} :user title :title mp3 :streamUrl image :image} tags]
     (metadata/tag-mp3 {:artist artist
                        :mp3 mp3
                        :title title
                        :album artist
                        :year "2012"
                        :image image})
     (str "music/" artist " - " title ".mp3")))

 (defn download-mp3s [url]
   "calls download-mp3 for every song found on the page"
   (map download-mp3 (parse/get-metainformations url)))

(defn -main
  "I don't do a whole lot."
  [& args]
  (str "blah"))

