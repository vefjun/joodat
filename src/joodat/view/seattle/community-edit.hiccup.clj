
  [:h1 "Edit community link" ]
  (form-to [:post "/seattle/community"]
   [:table

    (hidden-field "id" (:id *view-context*))

    [:tr 
        
        [:td   [:label "url"] ]
        [:td   [:input {:type "text" :name "url" :size 50 :value (:community/url (:community-entity *view-context*))}] ]
    ]
    [:tr
        [:td   [:label "name"]]
        [:td   [:input {:type "text" :name "name" :size 50 :value (:community/name (:community-entity *view-context*))}]]
    ]
    [:tr
        [:td   [:label "type"]]
        [:td   [:input {:type "text" :name "type" :size 50 :value (:community/type (:community-entity *view-context*))}]]
    ]
    [:tr
        [:td   [:label "orgtype"]]
        [:td   [:input {:type "text" :name "orgtype" :size 50 :value (:community/orgtype (:community-entity *view-context*))}]]
    ]
    [:tr
        [:td   [:label "categories"]]
        [:td
          (let [categories (:community/category (:community-entity *view-context*))] 
           (for [category categories]
             [:p category]
           )
          )
        ]
    ]
   [:tr
        [:td   [:label "new category"]]
        [:td   [:input {:type "text" :name "category" :size 50 }]]
    ]
    [:tr
        [:td   [:input {:type "submit" :name "submit" :size 20 :value "update"}]]
  
    ]
  ]
)
      
   
