
(ns auth.handlers
  (:require [auth.config :as config]
            [auth.db :as db]
            [buddy.sign.jwt :as jwt]))

(defn register!
  [{user :data}]
  (let [created-user (db/upsert-user user)]
    {:status 201 :body created-user}))

(defn login!
  [{user :data}]
  (if-let [checked-user (db/get-user-by-credentials user)]
    {:status 200 :body {:user checked-user
                        :token (jwt/sign user config/jwt-secret)}}
    {:status 404}))

(defn user
  [{{email :email} :params :as request}]
  (let [user (db/user email)]
    {:status 200 :body (or user {})}))
