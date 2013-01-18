(ns scripper.metadata
  (:import [org.jaudiotagger.audio							AudioFileIO]
           [org.jaudiotagger.tag								FieldKey]
           [org.jaudiotagger.tag.id3						AbstractID3v2Tag]
           [org.jaudiotagger.tag.id3.framebody	FrameBodyAPIC]
           [org.jaudiotagger.tag.id3						ID3v23Frame]
           [org.jaudiotagger.tag.datatype				DataTypes]
           [org.jaudiotagger.tag.reference			PictureTypes]
           [org.jaudiotagger.tag								TagField]
           [org.jaudiotagger.audio.mp3					MP3File]
           [org.jaudiotagger.tag.id3						ID3v23Tag]
           [java.io															ByteArrayOutputStream]
           [java.nio.channels										Channels])
  (:require [clojure.java.io :as io]
            [scripper.util :as util]))

 (defn set-image [image]
   "returns a id3v2-frame containing image"
   (let [body (FrameBodyAPIC.)
         frame (ID3v23Frame."APIC")]
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
   (let [{:keys [artist title year album image mp3]} tags
         filename (str "./resources/public/music/" artist " - " title ".mp3")
         file (-> mp3 util/download-binary  (create-mp3 filename) AudioFileIO/read)
         tag (.getTagOrCreateAndSetDefault file)]
     (.setField tag FieldKey/ARTIST artist)
     (.setField tag FieldKey/TITLE  title)
     (.setField tag FieldKey/YEAR   year)
     (.setField tag FieldKey/ALBUM  album)
     (.setField tag (-> image util/download-binary set-image))
     (.commit file)))
 
 (defn create-ID3v23-tag [tags]
   "takes a hash of tags and returns an ID3-v23-tag bytearray"
   (let [tag (ID3v23Tag.)
         {:keys [artist title year album image mp3]} tags   
         bytearray (ByteArrayOutputStream.)
         channel (Channels/newChannel bytearray)]
     (.addField tag FieldKey/ARTIST artist)
     (.addField tag FieldKey/TITLE  title)
     (.addField tag FieldKey/YEAR   year)
     (.addField tag FieldKey/ALBUM  album)
     (.setField tag (-> image util/download-binary set-image))
     (.write tag channel)
     (.close channel)
     (.toByteArray bytearray)))
