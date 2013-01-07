
(defn test-write []
  (tagmp3 {
    :mp3    "https://dl.dropbox.com/u/1994140/testmp3.mp3"
    :artist "TestArtist",
    :title  "TestTrack",
    :album  "TestAlbum",
    :year   "1000"
    :image  "https://dl.dropbox.com/u/1994140/P8270580n.jpg"}))

(defn test-fetch []
  (download-mp3s "https://soundcloud.com/porter-robinson/porter-robinson-mat-zo-easy"))

