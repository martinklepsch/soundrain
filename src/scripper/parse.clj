(ns scripper.parse
  (:require [net.cgrand.enlive-html :as html]
	          [clj-http.client :as client]
            [cheshire.core :as cheshire]))

(defn get-metainformation [song-url]
  "returns a complete hash with all available meta information"
  (let []

  ))

(defn get-scripts [source]
  "returns a list of all the javascripts embedded in source"
  (map :content (html/select source [:div#main-content :script])))

(defn extract-json [source]
  "takes an html-resource and extracts all json in the page"
  (let [re      #"\{[^}]*\{[^}]*\}[^}]*\}"
        scripts (get-scripts source)]
    (filter #(not (nil? %)) (flatten (map #(re-seq re %) (map str (flatten scripts)))))))

(defn get-artworks [html-resource]
  "returns a list of hashs with the artwork-images"
  (let [re #"http://i1.sndcdn.com/artworks[^\"]*\.jpg"]
    (map #(hash-map :image %)
      (filter #(not (nil? %))
	(map  #(first (re-seq re %))
	  (filter #(not (nil? %))
	    (map  #(:style (:attrs %))
	      (html/select html-resource [:a]))))))))

(defn get-source [url]
  "loads source of url and creates enlive html-resource from it"
  (html/html-resource (:body (client/get url {:as :stream}))))

(defn get-text-tags [source]
  "takes a html-resource and returns a list of hashs of all the text-tags"
  (filter :streamUrl  (map #(cheshire/parse-string % true) (extract-json source))))

(defn get-songs [url]
  "takes a url and returns hashs about the songs on the page"
  (let [source (get-source url)
        artworks (get-artworks source)
        text-tags (get-text-tags source)]
      text-tags))
