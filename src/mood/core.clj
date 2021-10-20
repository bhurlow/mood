(ns mood.core)

(require '[datomic.client.api :as d])

; (def client 
;  (d/client {:server-type :dev-local
;             :storage-dir "/Users/pretzel/code/mood"
;             :system "dev"}))

(def client 
 (d/client {:server-type :ion
            :region "us-east-1"
            :system "datomic-starter"
            :creds-profile "datomic-starter-account"
            :endpoint "https://o3wsjfjag5.execute-api.us-east-1.amazonaws.com"}))
            ; :storage-dir "/Users/pretzel/code/mood"
            ; :system "dev"}))


(d/create-database client {:db-name "moods-dev1"})

(def conn 
 (d/connect client {:db-name "moods-dev1"}))


(def mood-schema
 [{:db/ident :mood/tag
   :db/valueType :db.type/keyword
   :db/cardinality :db.cardinality/one}
  {:db/ident :mood/timestamp
   :db/valueType :db.type/instant
   :db/cardinality :db.cardinality/one}])

(defn transact-schema []
 (d/transact conn {:tx-data mood-schema}))


(defn add-moods []
 (d/transact conn 
             {:tx-data
              [{:mood/tag :foo}
               {:mood/tag :bar}]}))


(defn query-moods []
 (d/q
  '[:find ?m
    :where [?m :mood/tag :foo]]
  (d/db conn)))
