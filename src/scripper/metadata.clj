(ns scripper.metadata
  (:import [org.jaudiotagger.audio AudioFileIO]
           [org.jaudiotagger.tag FieldKey]
           [org.jaudiotagger.tag.id3 AbstractID3v2Tag]
           [org.jaudiotagger.tag.id3.framebody FrameBodyAPIC]
           [org.jaudiotagger.tag.id3 	ID3v23Frame]
           [org.jaudiotagger.tag.datatype DataTypes]
           [org.jaudiotagger.tag.reference PictureTypes]
           [org.jaudiotagger.tag TagField]
           [org.jaudiotagger.audio.mp3 MP3File])
  (:require [clojure.java.io :as io]
            [scripper.util :as util]))

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

 (defn tag-mp3 [tags]
   "creates the file and tags it with the given tags"
   (let [{artist :artist title :title year :year album :album image :image mp3 :mp3} tags
         filename (str "./resources/public/music/" artist " - " title ".mp3")
         file (-> mp3 util/download-binary  (create-mp3 filename) AudioFileIO/read)
         tag (.getTagOrCreateAndSetDefault file)]
     (.setField tag FieldKey/ARTIST artist)
     (.setField tag FieldKey/TITLE  title)
     (.setField tag FieldKey/YEAR   year)
     (.setField tag FieldKey/ALBUM  album)
     (.setField tag (-> image util/download-binary set-image))
     (.commit file)))


