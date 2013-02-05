(ns scripper.parse
  (:require [net.cgrand.enlive-html :as html]
	          [clj-http.client :as client]
            [cheshire.core :as cheshire]
            [scripper.metadata :as metadata] ))


(defn get-scripts [source]
  "returns a list of all the javascripts embedded in source"
  (flatten (map :content 
                (html/select source [:div#main-content :script]))))

(defn extract-json [source]
  "takes an html-resource and extracts all json in the page"
  (let [re      #"\{[^}]*\{[^}]*\}[^}]*\}"
        scripts (get-scripts source)]
    (filter (comp not nil?) 
            (map #(re-find re %) scripts))))

(defn get-source [url]
  "loads source of url and creates enlive html-resource from it"
  (html/html-resource (:body (client/get url {:as :stream}))))

(defn get-artworks [source text-tags]
  "returns a list of hashs with the artwork-images"
  (let [re #"http://i1.sndcdn.com/artworks[^\"]*\.jpg"]
    (map  
       (comp 
         	(partial hash-map :image)
          #(clojure.string/replace % #"badge|large" "t120x120")
	     		(partial re-find re)
        	:style 
        	:attrs )
       (html/select source [#{:a.artwork}]))))


(defn get-text-tags [source]
  "takes a html-resource and returns a list of hashs of all the text-tags"
  (map #(cheshire/parse-string % true) 
        (extract-json source)))

(defn get-metainformations [url]
  "takes a url and returns a list of hashes with the metainformation of the songs on the page"
  (let [source 		(get-source url)
        text-tags (get-text-tags source)
        artworks 	(get-artworks source text-tags)]
      (map #(merge %1 %2) text-tags artworks)))


 (defn get-mp3-metainformations [tags]
   "Filters the metadata"
   (let [{{artist :username} :user title :title mp3 :streamUrl image :image} tags
         filename (str (second (re-find #"/(\w+)\?" mp3)) ".128.mp3")]
    {:artist artist
     :mp3 mp3
     :title title
     :album artist
     :year "2012"
     :image image
     :filename filename}))

