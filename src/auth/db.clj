(ns auth.db
  (:require [buddy.hashers :refer [check encrypt]]))

(def db (atom {:users {}}))

(defn- sanitized-user
  [user]
  (dissoc user :password))

(defn user
  [email]
  (-> @db
      :users
      (get email)
      sanitized-user))

(defn- new-user
  [{password :password :as user}]
  (assoc user
         :password
         (encrypt password)))

(defn upsert-user
  [{email :email :as user}]
  (-> db
      (swap! #(assoc-in % [:users email] (new-user user)))
      (get-in [:users email])
      sanitized-user))

(defn get-user-by-credentials
  [{email          :email
    login-password :password}]
  (let [{saved-password :password :as user} (get-in @db [:users email])]
    (when (check login-password saved-password)
      (sanitized-user user))))
