;;Datomic queries and functions for seattle database
(ns joodat.model.seattle
 (:use
    [datomic.api :only [q db] :as d]
 )
)


(defn districts-query
 "returns all districts"
 [conn]
 (q '[:find ?n :where [?d :district/name ?n]] (db conn))
)

(defn district-neighborhoods-query
 "returns name and id of neighborhoods for a district with given name"
 [conn district-name]
 (q '[:find ?n ?ne :in $ ?di :where [?ne :neighborhood/name ?n]
                      [?ne :neighborhood/district ?d]
                      [?d :district/name ?di]
                      ] 
                      (db conn)
                      district-name)
)

(defn districtid-neighborhoods-query
 "returns ids of neighborhoods for a district with given id"
 [conn district-id]
 (q '[:find ?ne :in $ ?di :where  [?ne :neighborhood/district ?di]   
                      ] 
                      (db conn)
                      district-id)
)


(defn district-query
 "returns id of district with given name(" 
 [conn district]
 (q '[:find  ?d :in $ ?n :where [?d :district/name ?n]]
                            (db conn)
                            district)
)

(defn neighborhoodid-communities-query
 "returns all communities ids a given neighborhood id" 
 [conn neighborhoodid]
 (q '[:find  ?c :in $ ?id :where [?c :community/neighborhood ?id]
                             ]
                            (db conn)
                            neighborhoodid)
)

;; retract an entity
(defn delete-entity-query
  [conn id]
  (d/transact conn [[:db.fn/retractEntity id]])
)

(defn delete-neighborhood-communities-query
 "retracts all communities for neighborhood with given id"
 [conn neighborhood-id]
 (for [community (neighborhoodid-communities-query conn neighborhood-id)]
    (d/transact conn [[:db.fn/retractEntity (first community)]])
 )
)


(defn delete-district-neighborhoods-query
 "retracts all neigborhoods for district with given id"
 [conn district-id]
 (for [neighborhood (districtid-neighborhoods-query conn district-id)]
   (do 
     (doall (delete-neighborhood-communities-query conn (first neighborhood)))
     (d/transact conn [[:db.fn/retractEntity (first neighborhood)]])
   )
 )
)




;; retract a district entity
(defn district-delete-query
  "retracts a district with given name and all related neighborhoods"
  [conn district-name]
  (try
   (let [district-id (ffirst (district-query conn district-name))]
    (do
     ;;(if (districtid-neighborhoods-query conn district-id) 
       (doall (delete-district-neighborhoods-query conn district-id)))
     (delete-entity-query conn district-id)
    ;)
   )
  
   ;;(println "district " district-name " not found")
  
  (catch Exception e
   (println e)
   nil
  ))
)


(defn neighborhood-query
 "returns id of neighborhood with given name" 
 [conn neighborhood]
 (q '[:find  ?ne :in $ ?n :where [?ne :neighborhood/name ?n]]
                            (db conn)
                            neighborhood)
)


(defn neighborhood-communities-query
 "returns all communities names and urls for a given neighborhood" 
 [conn neighborhood]
 (q '[:find  ?n ?u :in $ ?nm :where [?c :community/name ?n]
                             [?c :community/url ?u]
                             [?c :community/neighborhood ?ne]
                             [?ne :neighborhood/name ?nm]
                             ]
                            (db conn)
                            neighborhood)
)

;; retract a neighborhood entity
(defn neighborhood-delete-query
  [conn neighborhood]
  (let [neighborhoodid (ffirst (neighborhood-query conn neighborhood))]
    (do
      (doall (delete-neighborhood-communities-query conn neighborhoodid))
      (d/transact conn [[:db.fn/retractEntity neighborhoodid]])
    )
  )
)



(defn neighborhoods-query
 "returns all neigborhood names"
 [conn]
 (q '[:find ?nm ?n :where [?n :neighborhood/name ?nm]] (db conn))
)



(defn communities-query
 "returns all communities names, urls" 
 [conn]
 (q '[:find  ?n ?u :where [?c :community/name ?n]
                             [?c :community/url ?u]]
                            (db conn))
)

(defn community-query
 "returns community with given link" 
 [conn url]
 (q '[:find  ?c :in $ ?n :where [?c :community/url ?n]]
                            (db conn)
                            url)
)

;; retract a community entity
(defn community-delete-query
  [conn url]
  (d/transact conn [[:db.fn/retractEntity (ffirst (community-query conn url))]])
)

(defn neighborhood-communities-query
 "returns all communities names and urls for a given neighborhood" 
 [conn neighborhood]
 (q '[:find  ?n ?u :in $ ?nm :where [?c :community/name ?n]
                             [?c :community/url ?u]
                             [?c :community/neighborhood ?ne]
                             [?ne :neighborhood/name ?nm]
                             ]
                            (db conn)
                            neighborhood)
)


(defn regions-query
 "returns all regions"
 [conn]
(q '[:find ?r :where [?d :district/region ?re]
                             [?re :db/ident ?r]
                             ]  (db conn))

)
