(ns scripper.core
  (:import [org.jaudiotagger.audio AudioFileIO]
           [org.jaudiotagger.tag FieldKey]
           [org.jaudiotagger.tag.id3 AbstractID3v2Tag]
           [org.jaudiotagger.tag.id3.framebody FrameBodyAPIC]
           [org.jaudiotagger.tag.id3 	ID3v23Frame]
           [org.jaudiotagger.tag.datatype DataTypes]
           [org.jaudiotagger.tag.reference PictureTypes]
           [org.jaudiotagger.tag TagField]
           [org.jaudiotagger.audio.mp3 MP3File]
           [org.apache.commons.lang StringEscapeUtils])
  (:require [clojure.java.io :as io]
	          [clj-http.client :as client]
            [scripper.metadata :as metadata]))


(defn download-binary [url]
  "downloads a file and returns it as a bytearray"
  (let [u (new java.net.URL url)
        filestream (.openStream u)]
    (org.apache.commons.io.IOUtils/toByteArray filestream)))

(defn merge-hashs [jsons artworks]
  "takes two lists of hashs and merge the corresponding hashs, returns a list of hashs"
  (map #(merge (first %) (second %)) (partition 2 (interleave artworks jsons))))

(defn extract-jsons [jscript]
  (let [re #"\{[^}]*\{[^}]*\}[^}]*\}"]
    (filter #(not (nil? %)) (flatten (map #(re-seq re %) (map str (flatten jscript)))))))

(defn filter-jsons [jsons]
  "returns only the json data that's in the given javascript"
  (let [re #"\"\w*\":\s*\"[^\"]*\""]
    (map #(re-seq re %) (flatten jsons))))

(defn json-to-hash [json]
  "takes a json in the form of \"key\":\"value\" and returns :key value, the jsons are already in a list"
  (apply hash-map (flatten
    (map #(list (keyword (first %)) (second %))
      (partition 2
	(map #(clojure.string/replace % #"\"" "")
	  (flatten (map #(clojure.string/split % #"\":\"") json))))))))


(defn download-mp3 [tags]
  "downloads the mp3 and sets the tags"
  (let [artist (StringEscapeUtils/unescapeHtml (:username tags))
        title (StringEscapeUtils/unescapeHtml(clojure.string/replace (:title tags) #"[\\/.=]" "-"))
        album (StringEscapeUtils/unescapeHtml(:username tags))
        year "2012"
        mp3 (:streamUrl tags)
        image (:image tags)]
    (metadata/tag-mp3 {:artist artist
                       :mp3 mp3
                       :title title
                       :album album
                       :year year
                       :image (clojure.string/replace image #"badge" "t120x120")})))

(defn download-mp3s [url]
  "calls download-mp3 for every song found on the page"
  (map download-mp3 (get-songs url)))

(defn -main
  "I don't do a whole lot."
  [& args]
  (test-fetch))

