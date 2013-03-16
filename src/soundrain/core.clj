(ns soundrain.core
  (:require 
    [clojure.java.io :as io]
    [soundrain.parse :as parse]
    [soundrain.util :as util]
    [ring.util.codec :as codec]
    [soundrain.view :as view])
  (:use compojure.core [hiccup core page form util]))


(defn mp3-json [url]
  "takes a url and returns a hash with the tag data and the b64 encoded mp3-tag-bytarray"
  (try 
    (let [metadata (parse/get-metainformation url)
          mp3-metadata (map parse/filter-metainformation metadata)
          html-site (map (comp (partial hash-map :html) view/song-form) 
                          mp3-metadata (range))
          images 
          (map 
            (comp (partial hash-map :image_data) codec/base64-encode util/download-binary :image) 
            mp3-metadata)]
      (map merge (map #(dissoc % :image) mp3-metadata) html-site images))
  ; Return empty list when an error occurs
  ; For example when a user enters a url 
  ; but the corresponding page on soundcloud
  ; does not exist
  (catch Exception e '())))






