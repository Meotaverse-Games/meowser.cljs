(ns meowser.i18n
  (:require [meowser.core :refer [scene]]
            [cljs.reader :as reader]
            [clojure.string :as str]))

(defonce config (atom {}))

(defn load-dictionary [target lang-opts]
  (doseq [[lang path] lang-opts]
    (.text (.-load (scene target)) (name lang) path))
  (swap! config assoc
         :cache-manager (-> (scene target) .-cache .-text)
         :langs (map first lang-opts)))

(defn setup-dictionary! []
  (when-not (:dictionary @config)
    (swap! config assoc :dictionary
           (into {}
                 (map (fn [lang]
                        [lang (reader/read-string (.get (:cache-manager @config) (name lang)))])
                      (:langs @config))))))

(defn set-lang! [lang]
  (swap! config assoc :lang lang))

(defn lang []
  (-> @config :lang))

(defn- replace-by-args [s args]
  (str/replace s
               #"\{(\w+)\}"
               #(some->> %
                         second
                         keyword
                         (get args)
                         str)))

(defn t [key & [args]]
  (let [keys (map keyword (str/split key #"\."))]
    (cond-> (get-in ((lang) (:dictionary @config)) keys)
      args (replace-by-args args))))
