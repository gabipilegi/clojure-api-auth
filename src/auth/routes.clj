(ns auth.routes
  (:require [auth.handlers :as handle]
            [auth.middlewares :as middlewares]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]))

(defroutes auth-routes
  (GET "/users/:email" request
       (-> handle/users
           middlewares/check-email-owner
           middlewares/auth-middleware
           middlewares/wrap-jwt-authentication-middleware))
  (POST "/register" request handle/register!)
  (POST "/login" _ handle/login!)
  (route/not-found "Not found"))
