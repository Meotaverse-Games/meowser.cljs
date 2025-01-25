(ns meowser.scene
  (:require [meowser.core :refer [scene sprite MeowserBase]]
            [shadow.cljs.modern :refer [defclass]]
            ["phaser" :as phaser]))

(defrecord MeowserScene [scene]
  MeowserBase
  (scene [this] scene)
  (sprite [this] nil))

(defn load-tilemap [target name path]
  (.tilemapTiledJSON (.-load (scene target)) name path))

(defn load-image [target name path]
  (.image (.-load (scene target)) name path))

(defn image-to-linear-filter [target image-name]
  (.setFilter (.get (.-textures (scene target)) image-name)
              (-> phaser .-Textures .-FilterMode .-NEAREST)))

(defn load-spritesheet [target name path {:keys [width height spacing margin start end]}]
  (.spritesheet (.-load ^js/Phaser.Scene (scene target))
                name path
                (clj->js {:frameWidth width :frameHeight height
                          :spacing (or spacing 0) :margin (or margin 0)
                          :startFrame (or start 0) :end end})))

(defn load-atlas [target id png-path anims-json sprite-json]
  (let [loader (.-load ^js/Phaser.Scene (scene target))]
    (.animation loader (str id "Data") anims-json)
    (.atlas loader id png-path sprite-json)))

(defn gen-tilemap [target name scale]
  {:map (.tilemap (.-make (scene target)) (js-obj "key" name))
   :scale scale})

(defn disable-cursor [target]
  (set! (-> target (scene) .-game .-canvas .-style .-cursor) "none"))

(defn delayed-callback [target delay callback]
  (.delayedCall (.-time (scene target)) delay callback))

(defn repeat-callback
  ([target delay callback]
   (repeat-callback target delay 0 callback))
  ([target delay repeat callback]
   (.addEvent (.-time (scene target))
              (clj->js {:delay delay :loop (= repeat 0) :repeat repeat
                        :callback callback}))))

(defn start-scene! [target next]
  (.start (.-scene (scene target)) (name next)))

(defn size [target]
  (let [scene (scene target)]
    [(-> scene .-scale .-width) (-> scene .-scale .-height)]))

(defn add-image! [target name & {:keys [position size]}]
  (let [image (.image (.-add (scene target)) (first position) (second position) name)]
    (when size
      (.setDisplaySize image (first size) (second size)))))

(defn move-to-object [self target position speed]
  (.moveToObject (.-physics (scene self))
                 (sprite target)
                 (clj->js position)
                 speed))

#_{:clj-kondo/ignore [:unresolved-symbol :invalid-arity]}
(defclass BridgeScene
  (extends phaser/Scene)
  (field meowser-scene-fn)
  (field callbacks)

  (constructor
   [this scene-name meowser-scene-fn callbacks]
   (super (clj->js {:key scene-name}))
   (set! (.-callbacks this) callbacks)
   (set! (.-meowser-scene-fn this) meowser-scene-fn)
   (when-let [init (:init callbacks)]
     (init this)))

  Object
  (preload [this]
           (when-let [preload (:preload callbacks)]
             (preload (meowser-scene-fn))))
  (create [this]
          (when-let [create (:create callbacks)]
            (create (meowser-scene-fn))))
  (update [this]
          (when-let [update (:update callbacks)]
            (update (meowser-scene-fn)))))

(defn gen-scene [scene-name callbacks]
  (let [meowser-scene-state (atom nil)

        bridge-scene
        (BridgeScene. scene-name (fn [] @meowser-scene-state) callbacks)

        meowser-scene (->MeowserScene bridge-scene)]
    (reset! meowser-scene-state meowser-scene)
    meowser-scene))
