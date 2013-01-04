(ns scripper.view
  (:use compojure.core scripper.core [hiccup core page form util] ))
  (use 'hiccup.bootstrap.page)
  
(defn html-doc [title & body] 
  (html 
    (doctype :html4) 
    [:html 
      [:head 
        [:title title]
        (include-css "/css/style.css")
        (include-bootstrap)] 
      [:body 
       [:div {:class "container"}
       [:h1  title]
        body]]])) 
        
(defn url-form []
  (html-doc "scripper" 
    [:form {:method "POST", :action (to-uri "/"), :class "form-search"}
      [:div.input-append
        [:input {:type "text" :name "url" :class "span5 input-large search-query"}]
        [:button {:type "submit" :class "btn" } "Search"]
      ]
    ]))
      
(defn song-form [tags]
  [:div.mp3
      [:img.img-polaroid.picture {:src (to-uri (:image tags))}]
      [:div {:class "artist"}  [:h3 (:username tags)]  
      [:div {:class "title"} (:title tags)]]
      ;;[:img {:src (to-uri (:image tags))}]
      ;;[:img {:src (to-uri (:waveformUrl tags))}]
      [:a {:href (download-mp3 tags)} "download"]
    ])
  
(defn results [url]
  (let [songs (get-songs url)]
    (html-doc "scripper"
      (map song-form songs))))
  
  
 