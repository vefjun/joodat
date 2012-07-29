(use 'joodo.env)

(def environment {
  :joodo.core.namespace "joodat.core"
  ; environment settings go here
  })

(swap! *env* merge environment)