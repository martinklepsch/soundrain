(ns scripper.core
  (:require [clojure.java.io :as io]
            [scripper.metadata :as metadata]
            [scripper.parse :as parse]
            [ring.util.codec :as codec]
            [scripper.view :as view]))



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

 (defn mp3-json [url]
   "takes a url and returns a hash with the tag data and the b64 encoded mp3-tag-bytarray"
   (let [metadata (parse/get-metainformations url)
         mp3-metadata (map parse/get-mp3-metainformations metadata)
         mp3-tags (map 
                   #(hash-map :tag (codec/base64-encode (metadata/create-ID3v23-tag %))) 
                   mp3-metadata)
         html-site (str (view/song-forms mp3-metadata))]
     (map #(merge %1 %2 %3) mp3-metadata mp3-tags {:html html-site})))
 




(defn -main
  "I don't do a whole lot."
  [& args]
  (str "blah"))

