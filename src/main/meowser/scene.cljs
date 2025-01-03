(ns meowser.scene
  (:require [shadow.cljs.modern :refer [defclass]]
            ["phaser" :as phaser]))

(defn load-tilemap [scene name path]
  (.tilemapTiledJSON (.-load scene) name path))

(defn load-image [scene name path]
  (.image (.-load scene) name path))

(defn image-to-linear-filter [scene image-name]
  (.setFilter (.get (.-textures scene) image-name)
              (-> phaser .-Textures .-FilterMode .-NEAREST)))

(defn load-spritesheet [^js/Phaser.Scene scene name path size]
  (let [[width height] size]
    (.spritesheet (.-load scene)
                  name path
                  (js-obj "frameWidth" width "frameHeight" height))))

(defn load-atlas [^js/Phaser.Scene scene id png-path anims-json sprite-json]
  (let [loader (.-load scene)]
    (.animation loader (str id "Data") anims-json)
    (.atlas loader id png-path sprite-json)))

(defn gen-tilemap [scene name scale]
  {:map (.tilemap (.-make scene) (js-obj "key" name))
   :scale scale})

(defn disable-cursor [scene]
  (set! (-> scene .-game .-canvas .-style .-cursor) "none"))

(defn delayed-callback [scene delay callback]
  (.delayedCall (.-time scene) delay callback))

(defn repeat-callback
  ([scene delay callback]
   (repeat-callback scene delay 0 callback))
  ([scene delay repeat callback]
   (.addEvent (.-time scene) (clj->js {:delay delay :loop (= repeat 0) :repeat repeat
                                       :callback callback}))))

(defn start-scene! [scene next]
  (.start (.-scene scene) (name next)))

(defn size [scene]
  [(-> scene .-scale .-width) (-> scene .-scale .-height)])

(defn add-image! [scene name & {:keys [position size]}]
  (let [image (.image (.-add scene) (first position) (second position) name)]
    (when size
      (.setDisplaySize image (first size) (second size)))))

(defclass BaseScene
  (extends phaser/Scene)
  (field callbacks)

  (constructor
   [this scene-name callbacks]
   (super (clj->js {:key scene-name}))
   (set! (.-callbacks this) callbacks)
   (when-let [init (:init callbacks)]
     (init this)))

  Object
  (preload [this]
           (when-let [preload (:preload callbacks)]
             (preload this)))
  (create [this]
          (when-let [create (:create callbacks)]
            (create this)))
  (update [this]
          (when-let [update (:update callbacks)]
            (update this))))

(defn gen-scene [scene-name callbacks]
  (BaseScene. scene-name callbacks))
