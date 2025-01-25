(ns meowser.keyboard
  (:require ["phaser" :as phaser]
            [meowser.core :refer [scene]]))

(defn key-down? [cursor-keys key]
  (.-isDown (get (js->clj cursor-keys) (name key))))

(defn key-up? [cursor-keys key]
  (.-isUp (get (js->clj cursor-keys) (name key))))

(def key-codes (-> phaser .-Input .-Keyboard .-KeyCodes js->clj))

(defn key->code [key]
  (get key-codes (-> key name (.toUpperCase))))

(defn key-just-down? [cursor-keys key]
  (.JustDown (-> phaser/Input .-Keyboard) (get (js->clj cursor-keys) (name key))))

(defn cursor-keys [target]
  (.createCursorKeys (-> target scene .-input .-keyboard)))

(defn add-keys [target keys-mapping]
  (.addKeys (-> target scene .-input .-keyboard)
            (clj->js (into {}
                           (map (fn [[key-name key]] [(name key-name) (key->code key)])
                                keys-mapping)))))
