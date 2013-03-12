(ns soundrain.view
  (:use	compojure.core
        hiccup.core
        hiccup.page
       	hiccup.bootstrap.page))

(def hostname "<i>soundrain.org</i>")
(defn fb [] 
  (html 
   [:div#fb-root]
		[:script {:type "text/javascript"}
     "(function(d, s, id) {
      var js, fjs = d.getElementsByTagName(s)[0];
      if (d.getElementById(id)) return;
      js = d.createElement(s); js.id = id;
      js.src = '//connect.facebook.net/en_US/all.js#xfbml=1';
      fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));"]))

(defn fb-like [] 
  (html [:div.fb-like 
         {:data-href "http://soundrain.org/" :data-send "false" 
          :data-layout "button_count" :data-width "450" 
          :data-show-faces "false" :data-font "segoe ui"}]))

(defn html-doc [title & body]
  (html
    (doctype :html5)
    [:html
      [:head
        [:title title]
        (include-js "/scripts/jquery-1.9.0.min.js")
        [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
        (include-bootstrap)
        (include-css "/css/style.css")
        (include-css "http://fonts.googleapis.com/css?family=Open+Sans")
        (include-js "/scripts/base64-binary.js")
      	(include-js "/scripts/soundrain.js")
        [:script {:type "text/javascript"}
          "var _gaq = _gaq || [];
          _gaq.push(['_setAccount', 'UA-3138561-9']);
          _gaq.push(['_trackPageview']);

          (function() {
            var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
            var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
          })();"]
        [:link {:rel "shortcut icon" :href "/favicon.ico"}]
       [:meta {:property "og:image" :content "http://soundrain.org/images/logotext.png"}]
       [:meta {:property "og:title" :content "soundrain.org"}]
       [:meta {:property "og:site_name" :content "soundrain"}]
       [:meta {:property "og:url" :content "http://soundrain.org"}]
       [:meta {:property "og:type" :content "website"}]
       [:link {:rel "image_src" :href "http://soundrain.org/images/logotext.png"}]]
      [:body
       (fb)
        [:div.container-fluid
            [:div.row body]]]]))

(defn about []
  (html
    [:div.modal.hide.fade {:id "aboutModal" :tabindex "-1" :role "dialog" :aria-labelledby "aboutModelLabel" :aria-hidden "true"}
      [:div.modal-header
        [:button.close {:data-dismiss "modal" :aria-hidden "true"} "x"]
        [:h3 "About"]]
    [:div.modal-body
      [:dl
        [:dt "About Soundrain"]
        [:dd
          "Being big fans of Soundcloud and listening almost exclusively to music streamed from Soundcloud we were interested if it is possible to download songs from Soundcloud using only new Web technologies. <br/><br/> Our initial plan was to let the user copy and paste a URL from Soundcloud into the site and then create a download links for every track which is listed on the URL you provided. Because raw MP3 files suck we wanted to combine this with adding an ID3 container (this is where all the metainformation like artist and title is stored). Our goal was to provide one single button that just downloads the MP3 with correct metainformation. <br/><br/>Unfortunately we hit some problems which made this impossible (yes impossible, not just hard). <br/><br/>The current solution is the best possible given our unwillingness to spend money on this little project which was just initiated to by our curiosity about the new HTML5 File API and Clojure. <br/><br/>If you are interested in how it works, you can find the source at: <a href=\"https://github.com/mklappstuhl/soundrain\">https://github.com/mklappstuhl/soundrain</a> <br/>soundrain was built by: Tom Nick, Maximilian Bachl, Martin Klepsch"]]]
    [:div.modal-footer
      [:button.btn {:data-dismiss "modal" :aria-hidden "true"} "Close"]]]))

(defn tos []
  (html
   [:div.modal.hide.fade {:id "tosModal" :tabindex "-1" :role "dialog" :aria-labelledby "tosModelLabel" :aria-hidden "true"}
      [:div.modal-header
        [:button.close {:data-dismiss "modal" :aria-hidden "true"} "x"]
        [:h3 hostname " Terms of Service Agreement"]]
      [:div.modal-body
        [:dl
          [:dt "Start"]
          [:dd "Welcome to " hostname ". By using (the \"Service\") " hostname " (the \"Web site\") you agree to be bound by these Terms of Use Agreement (this \"Agreement\").
                This Agreement sets out the legally binding terms of your use of the Web site and the Service and may be modified by " hostname " at any time and without prior notice, such modifications to be effective upon posting by " hostname " site. This Agreement includes " hostname " acceptable use policy for Content (the \"Content\") posted or accessed trough the Web site, " hostname " Privacy Policy, and any notices regarding the Web site.<br/> Please note that this website was ONLY designed to be a technical demo of the new HTML5 File API and the use of Clojure on the server side. It is only legal to download songs, when the creator of the corresponding content explicitly entitles you to do this or you are downloading your own works."]
          [:dt "Eligibilty" ]
          [:dd "By using the Web site, you represent and warrant that you have the right, authority, and capacity to enter into this Agreement and to abide by all of the terms and conditions of this Agreement."]
          [:dt "Term" ]
          [:dd "This Agreement will remain in full force and effect while you use the Web site."]
          [:dt "Non commercial use by users"]
          [:dd hostname " is intended for the personal use, unauthorized framing of or linking to the Web site will be investigated, and appropriate legal action will be taken, including without limitation, civil, criminal, and injunctive redress."]
          [:dt "Copyright policy"]
          [:dd "None of information, code, data, text, software, music, sound, photos, pictures, graphics, video, chat, messages, files, or other materials (the \"Content\") that can be accessed by using this site are hosted by this site; this site is not affiliate with the pages that you access. You only can use this site to examine your own pages, you acknowledge, warrant and represent that you legally own all intellectual property rights of the content that you access using this service. You may not post, distribute, or reproduce in any way any copyrighted material, trademarks, or other proprietary information without obtaining the prior written consent of the owner of such proprietary rights. You are solely responsible for the content that you access using this service."]
          [:dt "Privacy"]
          [:dd "Use of the Web site and/or the Service is also governed by our Privacy Policy."]
          [:dt "Disclaimers"]
          [:dd hostname " is not responsible for any incorrect or inaccurate Content posted on the Web site or in connection with the Service, whether caused by any of the equipment or programming associated with or utilized in the Service. " hostname " assumes no responsibility for any error, omission, interruption, deletion, defect, delay in operation or transmission, communications line failure, theft or destruction or unauthorized access to, or alteration of, user or communications. " hostname " is not responsible for any problems or technical malfunction of any telephone network or lines, computer online systems, servers or providers, computer equipment, software, failure of email or players on account of technical problems or traffic congestion on the Internet or at any web site or combination thereof, including injury or damage to users or to any other person's computer related to or resulting from participating or downloading materials in connection with the Web and/or in connection with the Service. Under no circumstances will " hostname " be responsible for any loss or damage, including personal injury or death, resulting from anyone's use of the Web site or the Service, any Content posted on the Web site, accessed trough the site. The Web site and the Service are provided \"AS-IS\" and " hostname " expressly disclaims any warranty of fitness for a particular purpose or non-infringement. " hostname " cannot guarantee and does not promise any specific results from use of the Web site and/or the Service. The service may be temporarily unavailable from time to time for maintenance or other reasons."]
          [:dt "Limitation on liability"]
          [:dd "In no event will " hostname "be liable to you or any third person for any indirect, consequential, exemplary, incidental, special or punitive damages, including also lost profits arising from your use of the Web site or the Service, even if " hostname " has been advised of the possibility of such damages."]
          [:dt "Disputes"]
          [:dd "If there is any dispute about or involving the Web site and/or the Service, by using the Web site, you agree that the dispute will be governed by the laws of Berlin/Germany without regard to its conflict of law provisions. You agree to personal jurisdiction by and venue in Berlin/Germany."]
          [:dt "Indemnity"]
          [:dd "You agree to indemnify and hold "hostname", its subsidiaries, affiliates, officers, agents, and other partners and employees, harmless from any loss, liability, claim, or demand, including reasonable attorney's fees, made by any third party due to or arising out of your use of the Service in violation of this Agreement and/or arising from a breach of this Agreement and/or any breach of your representations and warranties set forth above."]
          [:dt "No Agency"]
          [:dd "There is no agency, partnership, joint venture, employee-employer or franchisor-franchisee relationship between " hostname " and any User of the Service."]
          [:dt "Other" ]
          [:dd "This Agreement, accepted upon use of the Web site, contains the entire agreement between you and " hostname " regarding the use of the Web site and/or the Service. The failure of " hostname " to exercise or enforce any right or provision of these Terms of Service shall not constitute a waiver of such right or provision. If any provision of this Agreement is held invalid, the remainder of this Agreement shall continue in full force and effect. The section titles in these Terms of Service are for convenience only and have no legal or contractual effect."]
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
      [:div.modal-body.instructions-modal-body
        [:img {:src "images/instructions.png"}]]
      [:div.modal-footer
        [:button.btn {:data-dismiss "modal" :aria-hidden "true"} "Close"]]]))

(defn url-form []
  (html-doc "soundrain"
    [:div.page-header.span12      
     	[:h1 [:img {:src "images/logotext.png" :width "100" :height "100"}
        [:small "download soundcloud songs with ease"] ]]
    [:ul.inline
      [:li [:a.btn-link {:href "#instructionsModal" :role "button" :data-toggle "modal"} [:strong "Instructions"]]]
     	[:li [:a.btn-link {:href "#tosModal" :role "button" :data-toggle "modal"} "Terms Of Service"]]
     	[:li [:a.btn-link {:href "#aboutModal" :role "button" :data-toggle "modal"} "About"]]
      [:li (fb-like)]]]
    [:form#search-form.form-search.control-group.span12
      [:div.input-append
        [:input#search.search-query.input-xxlarge {
          :type "search"
          :placeholder "Paste a link to Soundcloud here"
          :autofocus ""
          :name "url"}]
        [:button#search-button.btn {:type "submit" :data-loading-text "Loading..." } "Get songs"]]
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
   (html
    [:div.mp3.span4 {:id (str "mp3-" n)}
      [:img.img-polaroid.picture {:src image}]
      [:div.text
        [:div.title artist]
        [:div.subtitle title]
        [:a.download-link
          {:href mp3 :download filename}
          [:i.icon-download] "Download file"]]])))


