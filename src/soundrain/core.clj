(ns soundrain.core
  (:require [clojure.java.io :as io]
            [soundrain.metadata :as metadata]
            [soundrain.parse :as parse]
            [ring.util.codec :as codec]
            [soundrain.view :as view])
  (:use compojure.core [hiccup core page form util]))


(defn mp3-json [url]
  "takes a url and returns a hash with the tag data and the b64 encoded mp3-tag-bytarray"
  (try 
    (let [metadata (parse/get-metainformations url)
          mp3-metadata (map parse/filter-metainformations metadata)
          mp3-tags (map 
                     (comp (partial hash-map :tag) codec/base64-encode metadata/create-ID3v23-tag) 
                     mp3-metadata)
          html-site (map (comp (partial hash-map :html) view/song-form) 
                          mp3-metadata (range))]
      (map merge mp3-metadata mp3-tags html-site))
  ; Return empty list when an error occurs
  ; For example when a user enters a url 
  ; but the corresponding page on soundcloud
  ; does not exist
  (catch Exception e '())))






