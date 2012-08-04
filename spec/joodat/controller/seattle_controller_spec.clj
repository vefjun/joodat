(ns joodat.controller.seattle-controller-spec
  (:use
    [speclj.core]
    [joodo.spec-helpers.controller]
    [joodat.controller.seattle-controller]
    ))

(describe "seattle Controller"

  (with-mock-rendering)
  (with-routes seattle-controller)


  (it "has test route"
    (let [response (do-get "/seattle/test")]
      (should= 200 (:status response))
      (should= "PASS" (:body response))
  ;    (should= nil @rendered-template)
  ;    (should= nil @rendered-context)
    ))
  

  
(it "redirects /seattle route"
    (let [response (should-redirect-to (do-get "/seattle") "seattle/index")]
      (should= nil (:status response))
      (should= nil @rendered-template)
   ))
 

  (it "has /seattle/index route"
    (let [response (do-get "/seattle/index")]
      (should= 200 (:status response))
      (should= "seattle/index" @rendered-template)
   ))

  (it "has /seattle/initdb route"
    (let [response (do-get "/seattle/initdb")]
      (should= 200 (:status response))
      (should= "seattle/initdb" @rendered-template)
   ))

  (it "has /seattle/communities route"
    (let [response (do-get "/seattle/communities")]
      (should= 200 (:status response))
      (should= "seattle/communities" @rendered-template)
   ))

  (it "has /seattle/districts route"
    (let [response (do-get "/seattle/districts")]
      (should= 200 (:status response))
      (should= "seattle/districts" @rendered-template)
   ))

  (it "has /seattle/neighborhoods route"
    (let [response (do-get "/seattle/neighborhoods")]
      (should= 200 (:status response))
      (should= "seattle/neighborhoods" @rendered-template)
   ))

 ;(it "has /seattle/district-neighborhoods route"
 ;   (let [response (do-get "/seattle/district-neighborhoods")]
 ;     (should= 200 (:status response))
 ;     (should= "seattle/district-neighborhoods" @rendered-template)
 ;  ))


  (it "has /seattle/neighborhood-communities route"
    (let [response (do-get "/seattle/neighborhood-communities/:id")]
      (should= 200 (:status response))
      (should= "seattle/neighborhood-communities" @rendered-template)
   ))

  (it "has /seattle/community/delete route"
    (let [response (do-get "/seattle/community/delete/:id")]
      (should= nil (:status response))
   ))

 (it "has /seattle/district/delete route"
    (let [response (do-get "/seattle/district/delete/:id")]
      (should= nil (:status response))
   ))

  )


(run-specs)
