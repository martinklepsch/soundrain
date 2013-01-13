 (ns scripper.util
   (:import [org.apache.commons.io IOUtils]))

 (defn download-binary [url]
   "downloads a file and returns it as a bytearray"
   (let [stream     (new java.net.URL url)
         filestream (.openStream stream)]
     (IOUtils/toByteArray filestream)))
