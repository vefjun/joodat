(ns joodat.controller.seattle-controller
  (:use
    [compojure.core]
    [clojure.pprint]
    [ring.util.response :only (redirect)]
    [joodo.views :only (*view-context* render-template)]
    [datomic.api :only [q db] :as d]
    [joodat.model.seattle]
    [hiccup.core]
    [hiccup.form-helpers]   
  )   
)

(def uri "datomic:mem://seattle")


(defn create-db
  "Creates an empty database and loads schema and data"
  [uri]

    (d/delete-database uri)
    (d/create-database uri)
    (let [conn (d/connect uri) 
       schema-tx (read-string (slurp "public/resources/seattle-schema.dtm"))
       data-tx   (read-string (slurp "public/resources/seattle-data0.dtm"))
       ]
      (d/transact conn schema-tx)
      (d/transact conn data-tx)
 
 ))

(defn initdb
 "calls create-db and renders the results"
 [uri]

 (if (create-db uri)
   (let
     [conn (d/connect uri)]
     (render-template "seattle/initdb" :regions-count (count (regions-query conn))
                                       :districts-count (count (districts-query conn))
                                       :neighborhoods-count (count (neighborhoods-query conn))
                                       :communities-count (count (communities-query conn)))
   )
   (render-template "seattle/fail")))

(defelem render-decorated-button [name & [icon-class]]
  [:a {:href "#"}
   [:button.btn.btn-small
    [:i {:class (or icon-class )}] name]])

(defn community-line [line]
 (html [:li
        [:span

         (form-to [:delete 
                    (str "/seattle/community/delete/" 
                      (clojure.string/replace (first line) " " "%20") 
                      "?url="
                      (clojure.string/replace (second line) " " "%20")
                    )
                  ]
               (render-decorated-button {:rel "tooltip" :title "Delete link"} "X" ))
         [:a {:href (second line)} (first line)]
        ]
       ])
)

(defn neighborhood-community-line [neighborhood line]
 (html [:li
        [:span

         (form-to 
             [:delete 
              (str "/seattle/neighborhood-community/delete/"
                (clojure.string/replace neighborhood " " "%20")
                "?url="
                (clojure.string/replace (second line) " " "%20")
              )
             ]
             (render-decorated-button  { :rel "tooltip" :title "Delete link"}  "X" ))
          [:a {:href (second line)} (first line)]
        ]
       ])
)

(defn district-line [line]
 (html [:li
        [:span 
         [:a {:href (str "/seattle/district-neighborhoods" 
                    "?district="
                    (clojure.string/replace (first line) " " "%20"))} (first line)]
        ]
       ])
)

(defn neighborhood-line [line]
 (html [:li
        [:span 
         [:a {:href (str "/seattle/neighborhood-communities/" (clojure.string/replace (first line) " " "%20"))} (first line)]
        ]
       ])
)


(defn render-communities
 "fetches all communities and renders"
 [uri]

 (try
  (let [conn (d/connect uri)]
    (render-template "seattle/communities" 
      :communities (apply str (html [:ul (map #(community-line %) (sort (communities-query conn))) ])))
  )
  (catch Exception e
      (println e)
      nil)
 )
)
  
(defn render-districts
 "fetches all districts and renders the names"
 [uri]

 (try
  (let [conn (d/connect uri)]
   (render-template "seattle/districts" 
    :districts   (apply str (html [:ul (map #(district-line %) (sort (districts-query conn))) ])) 
   )
  )
  (catch Exception e
      nil)

 )
)


(defn render-neighborhoods
 "renders neighborhood names"
 [uri]
 
 (try
  (let [conn (d/connect uri)]
   (render-template "seattle/neighborhoods" 
    :neighborhoods   (apply str (html [:ul (map #(neighborhood-line %) (sort (neighborhoods-query conn))) ]))   
   )
  )
  (catch Exception e
   (println e)
      nil)
 )
)

(defn render-district-neighborhoods
 "renders neighborhood links for given district"
 [uri district]

 (let [conn (d/connect uri)]
  (render-template "seattle/district-neighborhoods" 
   :neighborhoods (apply str (map #(neighborhood-line %) (sort (district-neighborhoods-query conn district))))
   :district district
  )
 )
)

(defn render-neighborhood-communities
 "renders community links for given neighborhood"
 [uri neighborhood]

 (let [conn (d/connect uri)]
  (render-template "seattle/neighborhood-communities" 
   :communities (apply str (map #(neighborhood-community-line neighborhood %) (sort (neighborhood-communities-query conn neighborhood))))
   :neighborhood neighborhood)
 )
)

(defn delete-community
  [uri url]
  (let [conn (d/connect uri)]
    (community-delete-query conn url)
    (render-communities uri)
  )
)

(defn delete-neighborhood-community
  [uri neighborhood url]
  (let [conn (d/connect uri)]
    (if (community-delete-query conn url)
      (redirect (str "/seattle/neighborhood-communities/" neighborhood))
      (render-template "seattle/fail")
    )
  )
)

(defroutes seattle-controller
  (GET "/seattle" [] (redirect "seattle/index"))
  (GET "/seattle/index" [] (render-template "seattle/index"))
  (GET "/seattle/initdb" [] (initdb uri))
  (GET "/seattle/districts" [] (render-districts uri))
  (GET "/seattle/neighborhoods" [] (render-neighborhoods uri))
  (GET "/seattle/communities" [] (render-communities uri))
  (GET "/seattle/district-neighborhoods" [district] (render-district-neighborhoods uri district))
  (GET "/seattle/neighborhood-communities/:id" [id] (render-neighborhood-communities uri id))
  (DELETE "/seattle/community/delete/:id" [id url] (delete-community uri url)) 
  (DELETE "/seattle/neighborhood-community/delete/:id" [id url] (delete-neighborhood-community uri id url))

  (context "/seattle" []
    (GET "/test" [] {:status 200 :body "PASS"})))
