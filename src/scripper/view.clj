	(ns scripper.view
  (use compojure.core [hiccup core page form util]))
  (use 'hiccup.bootstrap.page)

(defn html-doc [title & body]
  (html
    (doctype :html5)
    [:html
      [:head
        [:title title]
        (include-js "/script/jquery-1.9.0.min.js")
        [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
        (include-bootstrap)
        (include-css "/css/style.css")
        (include-css "http://fonts.googleapis.com/css?family=Open+Sans")
        (include-js "/script/base64-binary.js")
      	(include-js "/script/scripper.js")
        [:script {:type "text/javascript"}
          "var _gaq = _gaq || [];
          _gaq.push(['_setAccount', 'UA-3138561-9']);
          _gaq.push(['_trackPageview']);

          (function() {
            var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
            var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
          })();"]
      [:body
        [:div.container-fluid
            [:div.row body]
          ]
        ]
      ]]))

(defn url-form []
  (html-doc "Scripper"
    [:div.page-header.span12
      [:h1 "Scripper"
        [:small "download your favorite soundcloud songs with ease"]]]
    [:form#search-form.form-search.control-group.span12
      [:div.input-append
        [:input#search.search-query.input-xxlarge {
          :type "search"
          :placeholder "Paste a link to Soundcloud here"
          :autofocus ""
          :name "url"}]
        [:button#search-button.btn {:type "submit" :data-loading-text "Loading..." } "Search"]]
      [:span.help-inline]]
    [:div.search-results.span12]
    [:div#dropzone.drag.drag-inactive.span12
      "Now just drag & drop all MP3s here to add metainformation like artist and title"]
    ))



(defn song-form [tag n]
  (let
    [{:keys [artist title year album image mp3 filename]} tag]
    [:div.mp3.span4 {:id (str "mp3-" n)}
      [:img.img-polaroid.picture {:src image}]
      [:div.text
        [:div.title artist]
        [:div.subtitle title]
        [:a.download-link
          {:href mp3 :download filename}
          [:i.icon-download] "Download file from soundcloud"]
       ]
		]))



