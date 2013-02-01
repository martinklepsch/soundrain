	(ns scripper.view
  (:use compojure.core [hiccup core page form util]))
  (use 'hiccup.bootstrap.page)

(defn html-doc [title & body]
  (html
    (doctype :html5)
    [:html
      [:head
        [:title title]
        (include-js "/script/jquery-1.9.0.min.js")
        ; DEADLY SCRIPT
        ; (include-js "//connect.soundcloud.com/sdk.js")
        [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
        (include-bootstrap)
        (include-css "/css/style.css")
        (include-css "http://fonts.googleapis.com/css?family=Lato")
      	(include-js "/script/scripper.js")]
      [:body
        [:div {:class "container-fluid"}
          body]]]))

(defn url-form []
  (html-doc "Scripper"
    [:div.page-header.span12
      [:h1 "Scripper"
        [:small "download your favorite soundcloud songs with ease"]]]
    [:form.form-search.control-group.span12
      [:div.input-append
        [:input#search.search-query.input-xxlarge {
          :type "search"
          :placeholder "Paste a link to Soundcloud here or use our bookmarklet"
          :autofocus ""
          :name "url"}]
        [:button#search-button.btn {:type "submit" :data-loading-text "Loading..." } "Search"]]
      [:span.help-inline]]
    [:div.search-results.span12]
    ))



(defn song-form [tags]
    [:div.mp3
      [:img.img-polaroid.picture {:src (:image tags)}]
      [:div.text
        [:div.title (:artist tags)]
        [:div.subtitle (:title tags)]
        [:div.drag " → Drag'n'drop mp3 here"]
        [:a.btn.download-button
         {:href (:mp3 tags)}
         [:i.icon-download] " Rightclick → Save link as..."]
       ]
])

(defn song-forms [tags]
  (html (map song-form tags)))


