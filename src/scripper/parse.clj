(ns scripper.parse
  (:require [net.cgrand.enlive-html :as html]))

(defn get-javascripts [html-resource]
  "returns a list of all the javascripts embedded in the body"
  (map :content (html/select html-resource [:script])))

(defn get-artworks [html-resource]
  "returns a list of hashs with the artwork-images"
  (let [re #"http://i1.sndcdn.com/artworks[^\"]*\.jpg"]
    (map #(hash-map :image %)
      (filter #(not (nil? %))
	(map  #(first (re-seq re %))
	  (filter #(not (nil? %))
	    (map  #(:style (:attrs %))
	      (html/select html-resource [:a] ))))))))

(defn get-text-tags [html-resource]
  "takes a html-resource and returns a list of hashs of all the text-tags"
  (filter :streamUrl (map json-to-hash (filter-jsons (extract-jsons (get-javascripts html-resource))))))

(defn get-songs [url]
  "takes a url and returns hashs about the songs on the page"
  (let [source (html/html-resource (java.io.StringReader. (:body (client/get url))))
        artworks (get-artworks source)
        text-tags (get-text-tags source)]
      (merge-hashs artworks text-tags)))
