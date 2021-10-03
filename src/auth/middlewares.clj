(ns auth.middlewares
  (:require [auth.config :as config]
            [buddy.auth :refer [authenticated?]]
            [buddy.auth.backends :as backends]
            [buddy.auth.middleware :as auth.middleware]))

(defn body-params-to-data-middleware
  [handler]
  (fn [{:keys [body-params] :as request}]
    (-> request
        (assoc :data body-params)
        handler)))

(defn auth-middleware
  [handler]
  (fn [request]
    (if (authenticated? request)
      (handler request)
      {:status 401 :body {:error "Unauthorized"}})))

(defn check-email-owner
  [handler]
  (fn [{{identity-email :email} :identity
        {params-email :email}   :params :as request}]
    (if (= identity-email params-email)
      (handler request)
      {:status 403 :body "Forbidden"})))

(defn wrap-jwt-authentication-middleware
  [handler]
  (auth.middleware/wrap-authentication handler
                                       (backends/jws {:secret     config/jwt-secret
                                                      :token-name "token"})))
