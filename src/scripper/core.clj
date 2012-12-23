(ns scripper.core
  (:import [org.jaudiotagger.audio AudioFileIO]
           [org.jaudiotagger.tag FieldKey]
           [org.jaudiotagger.tag.id3 AbstractID3v2Tag]
           [org.jaudiotagger.tag.id3.framebody FrameBodyAPIC]
           [org.jaudiotagger.tag.id3 	ID3v23Frame]
           [org.jaudiotagger.tag.datatype DataTypes]
           [org.jaudiotagger.tag.reference PictureTypes]
           [org.jaudiotagger.tag TagField])
  (:require[clojure.java.io :as io]))
           
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
    
(defn set-image [image]
  ;; sets the image of the mp3 file, image should be an array of bytes
  (let [
  body (new FrameBodyAPIC)
  frame (new ID3v23Frame "APIC")]
    (. body setObjectValue DataTypes/OBJ_PICTURE_DATA image)
    (. body setObjectValue DataTypes/OBJ_PICTURE_TYPE PictureTypes/DEFAULT_ID)
    (. body setObjectValue DataTypes/OBJ_MIME_TYPE (str "image/jpg"))
    (. body setObjectValue DataTypes/OBJ_DESCRIPTION (str ""))
    (. frame setBody body)
    frame))

(defn createmp3 [mp3,filename]
  ;; don't know if there is a better way than writing the mp3 to disk and tag it.
  ;; mp3 should be a byte-array, maybe function can be rewritten with clojure IO
  (let [file (java.nio.channels.FileChannel/open 
	  (java.nio.file.Paths/get filename) 
	  (into-array '(java.nio.file.StandardOpenOption/CREATE_NEW java.nio.file.StandardOpenOption/WRITE)))]
    (. file write mp3)
    (. file close)))
    
(defn test-image-array []
  (let [
  file (new java.io.File "./ressources/noimage.jpg")
  filestream (new java.io.FileInputStream  file)
  data (byte-array (int (. file length)))]
    (. filestream read data)
    (. filestream close)
    data))
    
(defn download-image [url]
  (let [
    u (new java.net.URL url)
    filestream (. u openStream)]
    (. org.apache.commons.io.IOUtils toByteArray filestream)))
    
(defn tagmp3 [tags,filename]
  ;; tags the file with the name "filename" with tags
  (let [
  file (AudioFileIO/read (new java.io.File filename))
  tag (. file getTag)]
    (. tag setField FieldKey/ARTIST (:artist tags))
    (. tag setField FieldKey/TITLE  (:title tags))
    (. tag setField FieldKey/YEAR   (:year tags))
    (. tag setField FieldKey/ALBUM  (:album tags))
    (. tag deleteArtworkField)
    (. tag setField (set-image (download-image (:image tags))))
    (. file commit)))
    
    
(defn test-write []
  (tagmp3 {
    :artist "TestArtist", 
    :title "TestTrack", 
    :album "TestAlbum", 
    :year "1000" 
    :image "https://dl.dropbox.com/u/1994140/P8270580n.jpg"
    } 
    "./ressources/test_01_What_i_hope.mp3"))

      
(defn -main
  "I don't do a whole lot."
  [& args]
  (test-write))

