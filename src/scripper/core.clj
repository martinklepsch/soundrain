(ns scripper.core
  (:require [clojure.java.io :as io]
	          [clj-http.client :as client]
            [scripper.metadata :as metadata]))


; (defn merge-hashs [jsons artworks]
;   "takes two lists of hashs and merge the corresponding hashs, returns a list of hashs"
;   (map #(merge (first %) (second %)) (partition 2 (interleave artworks jsons))))



; (defn download-mp3 [tags]
;   "downloads the mp3 and sets the tags"
;   (let [artist (StringEscapeUtils/unescapeHtml (:username tags))
;         title (StringEscapeUtils/unescapeHtml(clojure.string/replace (:title tags) #"[\\/.=]" "-"))
;         album (StringEscapeUtils/unescapeHtml(:username tags))
;         year "2012"
;         mp3 (:streamUrl tags)
;         image (:image tags)]
;     (metadata/tag-mp3 {:artist artist
;                        :mp3 mp3
;                        :title title
;                        :album album
;                        :year year
;                        :image (clojure.string/replace image #"badge" "t120x120")})))

; (defn download-mp3s [url]
;   "calls download-mp3 for every song found on the page"
;   (map download-mp3 (get-songs url)))

(defn -main
  "I don't do a whole lot."
  [& args]
  (str "blah"))

