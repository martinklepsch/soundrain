(ns scripper.core
  (:import [org.jaudiotagger.audio AudioFileIO]
           [org.jaudiotagger.tag FieldKey]
           [org.jaudiotagger.tag.id3 AbstractID3v2Tag]
           [org.jaudiotagger.tag.id3.framebody FrameBodyAPIC]
           [org.jaudiotagger.tag.id3 	ID3v23Frame]
           [org.jaudiotagger.tag.datatype DataTypes]
           [org.jaudiotagger.tag.reference PictureTypes]
           [org.jaudiotagger.tag TagField]
           [org.jaudiotagger.audio.mp3 MP3File])
  (:require[clojure.java.io :as io]
	   [clj-http.client :as client]
           [net.cgrand.enlive-html :as html]))


(defn set-image [image]
  "returns a id3v2-frame containing image"
  (let [body (new FrameBodyAPIC)
        frame (new ID3v23Frame "APIC")]
    (.setObjectValue body DataTypes/OBJ_PICTURE_DATA image)
    (.setObjectValue body DataTypes/OBJ_PICTURE_TYPE PictureTypes/DEFAULT_ID)
    (.setObjectValue body DataTypes/OBJ_MIME_TYPE "image/jpg")
    (.setObjectValue body DataTypes/OBJ_DESCRIPTION "")
    (.setBody frame body)
    frame))

(defn create-mp3 [mp3 filename]
  "will create the new MP3 file"
  (with-open [output (new java.io.FileOutputStream filename)]
    (.write output mp3))
  (new java.io.File filename))

(defn download-binary [url]
  "downloads a file and returns it as a bytearray"
  (let [u (new java.net.URL url)
        filestream (.openStream u)]
    (org.apache.commons.io.IOUtils/toByteArray filestream)))

(defn tagmp3 [tags]
  "creates the file and tags it with the given tags"
  (let [
  filename (str "./" (:artist tags) " - " (:title tags) ".mp3")
  file (-> (:mp3 tags) download-binary  ,, (create-mp3 ,, filename) AudioFileIO/read)
  tag (.getTagOrCreateAndSetDefault file)]
    (.setField tag FieldKey/ARTIST (:artist tags))
    (.setField tag FieldKey/TITLE  (:title tags))
    (.setField tag FieldKey/YEAR   (:year tags))
    (.setField tag FieldKey/ALBUM  (:album tags))
    (.setField tag (-> (:image tags) download-binary set-image))
    (.commit file)))


(defn test-write []
  (tagmp3 {
    :mp3    "https://dl.dropbox.com/u/1994140/testmp3.mp3"
    :artist "TestArtist",
    :title  "TestTrack",
    :album  "TestAlbum",
    :year   "1000"
    :image  "https://dl.dropbox.com/u/1994140/P8270580n.jpg"}))
    
(defn get-javascripts [url]
  (let [body (:body (client/get url))]
    (map :content (html/select (html/html-resource (java.io.StringReader. body)) [:script]))))

(defn get-artworks [url]
  (let [body (:body (client/get url))
	re #"http://[a-zA-Z0-9?.:_\-=/\s()]*\.jpg"]
    (map #(hash-map :image (first (re-seq re %))) (filter #(not (nil? %)) (map #(:style (:attrs %)) 
    (html/select (html/html-resource (java.io.StringReader.  body))[:a] )))))) 

(defn merge-hashs [jsons artworks]
  (map #(merge (first %) (second %)) (partition 2 (interleave artworks jsons))))

(defn get-json-data [jscript]
  (let [re #"\"\w*\":\s*\"[a-zA-Z0-9?.:_\-=/\s()]*\""]
    (map #(re-seq re %) (map str (flatten jscript)))))

(defn json-to-hash [json]
  (apply hash-map (flatten (map #(list (keyword (first %)) (second %)) (partition 2 (map #(clojure.string/replace % #"\"" "") (flatten (map #(clojure.string/split % #"\":\"") json))))))))

(defn download-helper [tags]
  (let [
    artist (:username tags)
    title (:title tags)
    album "test"
    year "2012"
    mp3 (:streamUrl tags)
    image (:image tags)]
      (if (:streamUrl tags)
      (tagmp3 {
	:artist artist
	:mp3 mp3
	:title (clojure.string/replace title #"[\/.=]" "")
	:album album
	:year year
	:image image})
      (println "No streamUrl."))))
      
(defn download-mp3 [url]
  (map download-helper (merge-hashs (filter :streamUrl (map json-to-hash (get-json-data (get-javascripts url)))) (get-artworks url))))

(defn test-fetch []
  (download-mp3 "https://soundcloud.com/theeconomist/sponsor-excerpt-from-the"))
    
(defn -main
  "I don't do a whole lot."
  [& args]
  (test-fetch))

