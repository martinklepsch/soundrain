(ns scripper.parse
  (:require [net.cgrand.enlive-html :as html]
	          [clj-http.client :as client]
            [cheshire.core :as cheshire]))


(defn get-scripts [source]
  "returns a list of all the javascripts embedded in source"
  (map :content (html/select source [:div#main-content :script])))

(defn extract-json [source]
  "takes an html-resource and extracts all json in the page"
  (let [re      #"\{[^}]*\{[^}]*\}[^}]*\}"
        scripts (get-scripts source)]
    (filter #(not (nil? %)) (flatten (map #(re-seq re %) (map str (flatten scripts)))))))

(defn get-artworks [source]
  "returns a list of hashs with the artwork-images"
  (let [re #"http://i1.sndcdn.com/artworks[^\"]*\.jpg"]
	    (map  #(hash-map :image (clojure.string/replace (re-find re (:style (:attrs %))) #"badge" "t120x120"))
	      (html/select source [:a.artwork]))))

(defn get-source [url]
  "loads source of url and creates enlive html-resource from it"
  (html/html-resource (:body (client/get url {:as :stream}))))

(defn get-text-tags [source]
  "takes a html-resource and returns a list of hashs of all the text-tags"
  (filter :streamUrl  (map #(cheshire/parse-string % true) (extract-json source))))

(defn get-metainformations [url]
  "takes a url and returns a list of hashs with the metainformation of the songs on the page"
  (let [source 		(get-source url)
        artworks 	(get-artworks source)
        text-tags (get-text-tags source)]
      (map #(merge %1 %2) text-tags artworks)))
