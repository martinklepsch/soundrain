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
  (:require[clojure.java.io :as io]))


(defn set-image [image]
  "returns a id3v2-frame containing image"
  (let [body (new FrameBodyAPIC)
        frame (new ID3v23Frame "APIC")]
    (. body setObjectValue DataTypes/OBJ_PICTURE_DATA image)
    (. body setObjectValue DataTypes/OBJ_PICTURE_TYPE PictureTypes/DEFAULT_ID)
    (. body setObjectValue DataTypes/OBJ_MIME_TYPE "image/jpg")
    (. body setObjectValue DataTypes/OBJ_DESCRIPTION "")
    (. frame setBody body)
    frame))

(defn create-mp3 [mp3 filename]
  "will create the new MP3 file"
  (with-open [output (new java.io.FileOutputStream filename)]
    (.write output mp3))
  (new java.io.File filename))

(defn download-binary [url]
  "downloads a file and returns it as a bytearray"
  (let [u (new java.net.URL url)
        filestream (. u openStream)]
    (. org.apache.commons.io.IOUtils toByteArray filestream)))

(defn tagmp3 [tags]
  "creates the file and tags it with the given tags"
  (let [
  filename (str "./" (:artist tags) " - " (:title tags) ".mp3")
  file (-> (:mp3 tags) download-binary  ,, (create-mp3 ,, filename) AudioFileIO/read)
  tag (. file getTagOrCreateAndSetDefault)]
    (. tag setField FieldKey/ARTIST (:artist tags))
    (. tag setField FieldKey/TITLE  (:title tags))
    (. tag setField FieldKey/YEAR   (:year tags))
    (. tag setField FieldKey/ALBUM  (:album tags))
    (. tag setField (set-image (download-binary (:image tags))))
    (. file commit)))


(defn test-write []
  (tagmp3 {
    :mp3    "https://dl.dropbox.com/u/1994140/testmp3.mp3"
    :artist "TestArtist",
    :title  "TestTrack",
    :album  "TestAlbum",
    :year   "1000"
    :image  "https://dl.dropbox.com/u/1994140/P8270580n.jpg"}))

(defn -main
  "I don't do a whole lot."
  [& args]
  (test-write))

