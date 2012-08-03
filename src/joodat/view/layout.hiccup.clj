(doctype :html5)
[:html
 [:head
  [:meta {:http-equiv "Content-Type" :content "text/html;charset=utf-8"}]
  [:link {:rel "shortcut icon" :href "images/favicon.ico"}]
  [:link {:href "http://fonts.googleapis.com/css?family=EB+Garamond" :rel "stylesheet" :type "text/css"}]
  [:link {:href "http://fonts.googleapis.com/css?family=Open+Sans:300" :rel "stylesheet" :type "text/css"}]
  [:title "joodat"]
  (include-css "/stylesheets/reset.css")
  ;;(include-css "/stylesheets/joodat.css")
  ;;(include-css "/stylesheets/shCore.css")
  ;;(include-css "/stylesheets/shThemeDefault.css")
  (include-css "/stylesheets/screen.css")
  (include-js "/javascript/jquery-1.7.1.min.js")
  (include-js "/javascript/shCore.js")
  (include-js "/javascript/shBrushClojure.js")

 ]
 
 [:body
  [:table.body [:tr
   [:td.sidebar
     [:ul.navigation
     [:li [:a {:href "/"}                    [:span "home"]]]
     [:li [:a {:href "/seattle/initdb"}      [:span "initdb"]]]
     [:li [:a {:href "/seattle/districts"}   [:span "districts"]]]
     [:li [:a {:href "/seattle/neighborhoods"}   [:span "neighborhoods"]]]
     [:li [:a {:href "/seattle/communities"} [:span "community links"]]]
    ]
   ]


   [:td.content
    (eval (:template-body joodo.views/*view-context*))
   ]]
  ]
 ]
]
