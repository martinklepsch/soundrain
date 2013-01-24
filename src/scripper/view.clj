	(ns scripper.view
  (:use compojure.core [hiccup core page form util]))
  (use 'hiccup.bootstrap.page)
  
(defn html-doc [title & body] 
  (html 
    (doctype :html5) 
    [:html 
      [:head 
        [:title title]
        (include-css "/css/style.css")
        (include-js "/script/jquery-1.9.0.min.js")
        [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
        (include-bootstrap)
      	(include-js "/script/scripper.js")] 
      [:body 
        [:div {:class "container-fluid"}
          body]]])) 
        
(defn url-form []
  (html-doc "Scripper"
    [:div.page-header.span12
      [:h1 "Scripper"
        [:small " the freshest thing around"]]]
    [:div.form-search.control-group.span12
      [:div.input-append
        [:input#search.search-query {:type "search" :autofocus "" :name "url"}]
        [:button#search-button.btn.btn-primary {:type "button" :data-loading-text "Loading..." } "Search"]]
      [:span.help-inline]]
    [:div.search-results.span12]
    ))



(defn song-form [tags]
    [:div.mp3
      [:img.img-polaroid.picture {:src (:image tags)}]
      [:div.text
        [:div.title (:artist tags)]
        [:div.subtitle (:title tags)]
        ;;[:img {:src (to-uri (:image tags))}]
        ;;[:img {:src (to-uri (:waveformUrl tags))}]
       	[:button.btn.download-button [:i.icon-download] " download"]
       ]
])
  
  (defn song-forms [tags]
  (html (map song-form tags)))
  
 