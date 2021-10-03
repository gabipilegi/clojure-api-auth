(ns auth.core
  (:require [auth.config :as config]
            [auth.middlewares :as middlewares]
            [auth.routes :as routes]
            muuntaja.middleware
            [ring.adapter.jetty :refer [run-jetty]]))

(defn run-app
  []
  (run-jetty (-> auth.routes/auth-routes
                 middlewares/body-params-to-data-middleware
                 muuntaja.middleware/wrap-format)
             {:join? false
              :port config/port}))

(defn stop-app
  [app]
  (.stop app))

(comment
  (def app (run-app))
  (stop-app app))
