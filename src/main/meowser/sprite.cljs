(ns meowser.sprite
  (:require [meowser.core :refer [MeowserBase scene sprite]]))

(defrecord MeowserSprite [sprite]
  MeowserBase
  (scene [_this] (.-scene sprite))
  (sprite [_this] sprite))

(defn set-visible! [{:keys [sprite]} visible?]
  (set! (.-visible sprite) visible?))

(defn flip-x! [{:keys [sprite]} flip-x?]
  (set! (.-flipX sprite) flip-x?))

(defn velocity [{:keys [sprite]}]
  (let [velocity (-> sprite .-body .-velocity)]
    {:x (.-x velocity)
     :y (.-y velocity)}))

(defn set-velocity! [{:keys [sprite]} {:keys [x y]}]
  (when x
    (.setVelocityX sprite x))
  (when y
    (.setVelocityY sprite y)))

(defn add-x [{:keys [sprite]} speed]
  (let [velocity (-> sprite .-body .-velocity)]
    (set! (.-x velocity) (+ (.-x velocity) speed))))

(defn add-y [{:keys [sprite]} speed]
  (let [velocity (-> sprite .-body .-velocity)]
    (set! (.-y velocity) (+ (.-y velocity) speed))))

(defn position [{:keys [sprite]}]
  {:x (.-x sprite)
   :y (.-y sprite)})

(defn set-position! [{:keys [sprite]} {:keys [x, y]}]
  (.setPosition sprite x, y))

(defn play-anim [{:keys [sprite]} name]
  (.play sprite (clj->js {:key name}) true))

(defn follow-camera [{:keys [scene sprite]} & [zoom]]
  (let [camera (-> scene .-cameras .-main)]
    (when zoom
      (.zoomTo camera zoom))
    (.startFollow camera sprite true)))

(defn set-size! [{:keys [sprite]} width height]
  (.setSize sprite width height))

(defn set-body-size! [{:keys [sprite]} width height]
  (.setBodySize sprite width height))

(defn set-scale! [{:keys [sprite] :as v} x & [y]]
  (.setScale sprite x, (or y x)))

(defn set-offset! [{:keys [sprite]} x y]
  (.setOffset sprite x y))

(defn set-origin! [{:keys [sprite]} x & [y]]
  (.setOrigin sprite x (or y x)))

(defn set-allow-gravity! [{:keys [sprite]} flag]
  (.setAllowGravity (.-body sprite) flag))

(defn set-immovable! [{:keys [sprite]} flag]
  (.setImmovable sprite flag))

(defn add! [target add-target]
  (.add (sprite target) (sprite add-target)))

(defn set-pointer-events! [{:keys [sprite]} events]
  (.setInteractive sprite)
  (doseq [[event handler] events]
    (.on sprite (event {:down "pointerdown"
                        :over "pointerover"
                        :out "pointerout"})
         handler)))

(defn set-interactive-events! [{:keys [sprite]} {:keys [hit-area draggable? drop-zone?]} events]
  (let [opts {:draggable draggable?
              :dropZone drop-zone?}
        opts (cond-> opts
               hit-area (assoc :hitArea (let [{:keys [x, y, width, height]} hit-area]
                                          (js/Phaser.Geom.Rectangle. x, y, width, height))
                               :hitAreaCallback js/Phaser.Geom.Rectangle.Contains))]
    (.setInteractive sprite (clj->js opts)))
  (doseq [[event handler] events]
    (.on sprite (event {:drag-start "dragstart"
                        :drag "drag"
                        :drag-end "dragend"
                        :drag-enter "dragenter"
                        :drag-leave "dragleave"})
         handler)))

(defn set-drop-zone! [{:keys [sprite]} flag]
  (set! (.-dropZone (.-input sprite)) flag))

(defn bring-to-top! [{:keys [sprite]} {target-sprite :sprite}]
  (.bringToTop sprite target-sprite))

(defn set-tint! [{:keys [sprite]} color]
  (.setTint sprite color))

(defn clear-tint! [{:keys [sprite]}]
  (.clearTint sprite))

(defn collider-with-sprite [{:keys [sprite] :as target} {target-sprite :sprite} & [collide-fn]]
  (.collider (-> (scene target) .-physics .-add) sprite target-sprite collide-fn))

(defn- _gen-sprite [target f]
  (let [scene (scene target)
        new-sprite (f scene)]
    (when-let [sprite (sprite target)]
      (.add sprite new-sprite))
    (->MeowserSprite new-sprite)))

(defn gen-sprite [target & {:keys [key x y]}]
  (_gen-sprite
   target
   #(.sprite (-> % .-physics .-add) x y key)))

(defn gen-frame-index-sprite [target & {:keys [key frame-index x y]}]
  (_gen-sprite
   target
   #(.sprite (-> % .-physics .-add) x, y key frame-index)))

(defn gen-no-display-sprite [target & {:keys [x y width height]}]
  (_gen-sprite
   target
   #(let [sprite (.zone (-> ^js/Phaser.Scene % .-add) x, y, width, height)]
      (.existing (-> % .-physics .-add)
                 sprite
                 false))))

(defn gen-container [target & {:keys [x y]}]
  (_gen-sprite
   target
   #(.container (-> ^js/Phaser.Scene % .-add) x y)))