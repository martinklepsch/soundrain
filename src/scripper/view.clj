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
          })();"]]
      [:body
        [:div.container-fluid
            [:div.row body]
          ]
        ]
      ]))

(defn about []
  (html
    [:div.modal.hide.fade {:id "aboutModal" :tabindex "-1" :role "dialog" :aria-labelledby "aboutModelLabel" :aria-hidden "true"}
      [:div.modal-header
        [:button.close {:data-dismiss "modal" :aria-hidden "true"} "x"]
        [:h3 "About"]]
    [:div.modal-body
      [:dl
        [:dt "About Scripper"]
        [:dd
          "Being big fans of Soundcloud and listening almost exclusively to music streamed from Soundcloud we were interested if it is possible to download songs from Soundcloud using only new Web technologies. <br/><br/> Our initial plan was to let the user copy and paste a URL from Soundcloud into the site and then create a download links for every track which is listed on the URL you provided. Because raw MP3 files suck we wanted to combine this with adding an ID3 container (this is where all the metainformation like artist and title is stored). Our goal was to provide one single button that just downloads the MP3 with correct metainformation. <br/><br/>Unfortunately we hit some problems which made this impossible (yes impossible, not just hard). <br/><br/>The current solution is the best possible given our unwillingness to spend money on this little project which was just initiated to by our curiosity about the new HTML5 File API and Clojure. <br/><br/>If you are interested in how it works, you can find the source at: <a href=\"https://github.com/mklappstuhl/scripper\">https://github.com/mklappstuhl/scripper</a> <br/>Scripper was built by: Tom Nick, Maximilian Bachl, Martin Klepsch"
          ; "Being big fans of Soundcloud and listening almost exclusively to music streamed from Soundcloud we missed the possibility to access our favorite tunes on the go and offline in general without killing our 3G plan. <br/><br/> Our initial plan was to let you copy and paste a URL from Soundcloud into our site and provide you with download links for every track which is listed on the URL you provided. Because raw MP3 files suck we wanted to combine this with adding an ID3 container (this is where all the metainformation like artist and title is stored). Our goal was to provide one single button that just downloads the MP3 with correct metainformation. <br/><br/>Unfortunately we hit some problems which made this impossible (yes impossible, not just hard). <br/><br/>The current solution is the best possible given our unwillingness to spend money on this little project which was just for fun anyway. <br/><br/>If you are interested in how it works, you can find the source here: <a href=\" https://github.com/mklappstuhl/scripper\"></a> <br/>Scripper was built by: Tom Nick, Maximilian Bachl, Martin Klepsch"
    ]]]]))

(defn tos []
  (html
   [:div.modal.hide.fade {:id "tosModal" :tabindex "-1" :role "dialog" :aria-labelledby "tosModelLabel" :aria-hidden "true"}
      [:div.modal-header
        [:button.close {:data-dismiss "modal" :aria-hidden "true"} "x"]
        [:h3 "scripper.herokuapp.com Terms of Service Agreement"]]
      [:div.modal-body
        [:dl
            [:dt "Start"]
            [:dd "Welcome to scripper.herokuapp.com. By using (the \"Service\") scripper.herokuapp.com (the \"Web site\") you agree to be bound by these Terms of Use Agreement (this \"Agreement\").
                  This Agreement sets out the legally binding terms of your use of the Web site and the Service and may be modified by scripper.herokuapp.com at any time and without prior notice, such modifications to be effective upon posting by scripper.herokuapp.com site. This Agreement includes scripper.herokuapp.com acceptable use policy for Content (the \"Content\") posted or accessed trough the Web site, scripper.herokuapp.com Privacy Policy, and any notices regarding the Web site.<br/> Please note that this website was ONLY designed to be a technical demo of the new HTML5 File API and the use of Clojure on the server side. It is only legal to download songs, when the creator of the corresponding content explicitly entitles you to do this or you are downloading your own works."]
            [:dt "Eligibilty" ]
            [:dd "By using the Web site, you represent and warrant that you have the right, authority, and capacity to enter into this Agreement and to abide by all of the terms and conditions of this Agreement."]
            [:dt "Term" ]
            [:dd "This Agreement will remain in full force and effect while you use the Web site."]
            [:dt "Non commercial use by users"]
            [:dd "scripper.herokuapp.com is intended for the personal use, unauthorized framing of or linking to the Web site will be investigated, and appropriate legal action will be taken, including without limitation, civil, criminal, and injunctive redress."]
            [:dt "Copyright policy"]
            [:dd "None of information, code, data, text, software, music, sound, photos, pictures, graphics, video, chat, messages, files, or other materials (the \"Content\") that can be accessed by using this site are hosted by this site; this site is not affiliate with the pages that you access. You only can use this site to examine your own pages, you acknowledge, warrant and represent that you legally own all intellectual property rights of the content that you access using this service. You may not post, distribute, or reproduce in any way any copyrighted material, trademarks, or other proprietary information without obtaining the prior written consent of the owner of such proprietary rights. You are solely responsible for the content that you access using this service."]
            [:dt "Privacy"]
            [:dd "Use of the Web site and/or the Service is also governed by our Privacy Policy."]
            [:dt "Disclaimers"]
            [:dd "scripper.herokuapp.com is not responsible for any incorrect or inaccurate Content posted on the Web site or in connection with the Service, whether caused by any of the equipment or programming associated with or utilized in the Service. scripper.herokuapp.com assumes no responsibility for any error, omission, interruption, deletion, defect, delay in operation or transmission, communications line failure, theft or destruction or unauthorized access to, or alteration of, user or communications. scripper.herokuapp.com is not responsible for any problems or technical malfunction of any telephone network or lines, computer online systems, servers or providers, computer equipment, software, failure of email or players on account of technical problems or traffic congestion on the Internet or at any web site or combination thereof, including injury or damage to users or to any other person's computer related to or resulting from participating or downloading materials in connection with the Web and/or in connection with the Service. Under no circumstances will scripper.herokuapp.com be responsible for any loss or damage, including personal injury or death, resulting from anyone's use of the Web site or the Service, any Content posted on the Web site, accessed trough the site. The Web site and the Service are provided \"AS-IS\" and scripper.herokuapp.com expressly disclaims any warranty of fitness for a particular purpose or non-infringement. scripper.herokuapp.com cannot guarantee and does not promise any specific results from use of the Web site and/or the Service. The service may be temporarily unavailable from time to time for maintenance or other reasons."]
            [:dt "Limitation on liability"]
            [:dd "In no event will scripper.herokuapp.com be liable to you or any third person for any indirect, consequential, exemplary, incidental, special or punitive damages, including also lost profits arising from your use of the Web site or the Service, even if scripper.herokuapp.com has been advised of the possibility of such damages."]
            [:dt "Disputes"]
            [:dd "If there is any dispute about or involving the Web site and/or the Service, by using the Web site, you agree that the dispute will be governed by the laws of Berlin/Germany without regard to its conflict of law provisions. You agree to personal jurisdiction by and venue in Berlin/Germany."]
            [:dt "Indemnity"]
            [:dd "You agree to indemnify and hold scripper.herokuapp.com, its subsidiaries, affiliates, officers, agents, and other partners and employees, harmless from any loss, liability, claim, or demand, including reasonable attorney's fees, made by any third party due to or arising out of your use of the Service in violation of this Agreement and/or arising from a breach of this Agreement and/or any breach of your representations and warranties set forth above."]
            [:dt "No Agency"]
            [:dd "There is no agency, partnership, joint venture, employee-employer or franchisor-franchisee relationship between scripper.herokuapp.com and any User of the Service."]
            [:dt "Other" ]
            [:dd "This Agreement, accepted upon use of the Web site, contains the entire agreement between you and scripper.herokuapp.com regarding the use of the Web site and/or the Service. The failure of scripper.herokuapp.com to exercise or enforce any right or provision of these Terms of Service shall not constitute a waiver of such right or provision. If any provision of this Agreement is held invalid, the remainder of this Agreement shall continue in full force and effect. The section titles in these Terms of Service are for convenience only and have no legal or contractual effect."]
            [:dt "Agreement"]
            [:dd "I have read the above policy and will agree to all of the provisions above."]]]
      [:div.modal-footer
       [:button.btn {:data-dismiss "modal" :aria-hidden "true"} "Close"]]]))

(defn instructions []
  (html
    [:div.modal.hide.fade {:id "instructionsModal" :tabindex "-1" :role "dialog" :aria-labelledby "instructionsModelLabel" :aria-hidden "true"}
      [:div.modal-header
        [:button.close {:data-dismiss "modal" :aria-hidden "true"} "x"]
        [:h3 "Instructions"]]
    [:div.modal-body
      [:dl
        [:dt "Instructions"]
        [:dd
          [:ol
            [:li "Download all the files on the page that you like."]
            [:li "Drag all the downloaded songs (named something like \"h3ShIpHVBzrw.128.mp3\" into the box that opened on the bottom of the page."]
            [:li "Now the backgroud of the songs on the page that you dragged and dropped into the box changed to mint green. <em>Download and enjoy</em> :)"]
            ]]]]]))

(defn url-form []
  (html-doc "Scripper"
    [:div.page-header.span12
      [:h1 "Scripper"
        [:small "download soundcloud songs with ease"]]
     [:ul.inline
      [:li [:a.btn-link {:href "#instructionsModal" :role "button" :data-toggle "modal"} [:strong "Instructions"]]]
     	[:li [:a.btn-link {:href "#tosModal" :role "button" :data-toggle "modal"} "Terms Of Service"]]
     	[:li [:a.btn-link {:href "#aboutModal" :role "button" :data-toggle "modal"} "About"]]]
     ]
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
    (instructions)
    (tos)
    (about)))



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
          [:i.icon-download] "Download file"]
       ]
		]))



