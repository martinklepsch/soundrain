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
                  
(defn -main
  "I don't do a whole lot."
  [& args]
  (print (:artist (:tags (metadata "./ressources/test_01_What_i_hope.mp3")))))

