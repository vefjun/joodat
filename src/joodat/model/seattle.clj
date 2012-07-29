;;Datomic queries for seattle database
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
 "returns all neighborhoods for a given district"
 [conn district]
 (q '[:find ?n :in $ ?di :where [?ne :neighborhood/name ?n]
                      [?ne :neighborhood/district ?d]
                      [?d :district/name ?di]
                      ] 
                      (db conn)
                      district)
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
