(ns scripper.core
  (:import [org.jaudiotagger.audio AudioFileIO]
           [org.jaudiotagger.tag FieldKey]))
           
(defn tags [file]
  (let [fields (apply conj {} (map (fn [n] [(keyword (. (. n toString) toLowerCase)) n]) (. FieldKey values)))
	tag (. file (getTag))]
    (apply conj {}
	    (filter (fn [[name val]] (and val (not (empty? val))))
		    (map (fn [[name val]]
			  [name (seq (map #(. % getContent) (. tag (getFields val))))])
		      fields)))))
 
(defn audioheader [file]
  (bean (. file (getAudioHeader))))
 
(defn metadata [filename]
  (let [file (AudioFileIO/read (new java.io.File filename))]
    {:tags (tags file)
     :audioheader (audioheader file)}))
     
(defn tagmp3 [tags,filename]
  ;; tags the file with the name "filename" with tags
  (let [
  file (AudioFileIO/read (new java.io.File filename))
  tag (. file getTag)]
    (. tag setField FieldKey/ARTIST (str(:artist tags)))
    (. tag setField FieldKey/TITLE  (str(:title tags)))
    (. tag setField FieldKey/YEAR   (str(:year tags)))
    (. tag setField FieldKey/ALBUM  (str(:album tags)))
    (. file commit)))

(defn createmp3 [mp3,filename]
  ;; don't know if there is a better way than writing the mp3 to disk and tag it.
  ;; mp3 should be a byte-array, maybe function can be rewritten with clojure IO
  (let [
    file (java.nio.channels.FileChannel/open (java.nio.file.Paths/get filename) (into-array '(java.nio.file.StandardOpenOption/CREATE_NEW java.nio.file.StandardOpenOption/WRITE)))]
    (. file write mp3)
    (. file close)))

(defn test-write []
  (tagmp3 {:artist "TestArtist", :title "TestTrack", :album "TestAlbum", :year "1000"} "./ressources/test_01_What_i_hope.mp3"))
  
(defn -main
  "I don't do a whole lot."
  [& args]
  (test-write))

