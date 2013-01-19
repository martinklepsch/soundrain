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
        (include-bootstrap)
      	(include-js "/script/scripper.js")] 
      [:body 
       [:div {:class "container"}
       [:h1  title]
        body]]])) 
        
(defn url-form []
  (html-doc "scripper"
     [:div.form-search.control-group
      [:div.input-append
        [:input {:type "text" :name "url" :class "span5 input-large search-query"}]
        [:button {:type "button" :class "btn" } "Search"]]
      [:span.help-inline]
    ]))



(defn song-form [tags]
    [:div.mp3
      [:img.img-polaroid.picture {:src (:image tags)}]
      [:div.text
        [:div.title (:artist tags)]
        [:div.subtitle (:title tags)]
        ;;[:img {:src (to-uri (:image tags))}]
        ;;[:img {:src (to-uri (:waveformUrl tags))}]
       	[:button.btn.btn-primary [:i.icon-download.icon-white]    "download"]
       ]
])
  
  (defn song-forms [tags]
  (html (map song-form tags)))
  
 