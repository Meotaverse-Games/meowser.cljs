(ns meowser.sprite
  (:require ["phaser" :as phaser]))

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

(defn play-anim [{:keys [sprite]} name]
  (.play sprite (clj->js {:key name}) true))

(defn follow-camera [{:keys [scene sprite]}]
  (.startFollow (-> scene .-cameras .-main) sprite true))

(defn set-size! [{:keys [sprite]} width height]
  (.setSize sprite width height))

(defn set-body-size! [{:keys [sprite]} width height]
  (.setBodySize sprite width height))

(defn set-offset! [{:keys [sprite]} x y]
  (.setOffset sprite x y))

(defn set-allow-gravity! [{:keys [sprite]} flag]
  (.setAllowGravity (.-body sprite) flag))

(defn add! [{:keys [sprite]} target]
  (.add sprite target))

(defn set-pointer-events! [{:keys [sprite]} events]
  (.setInteractive sprite)
  (doseq [[event handler] events]
    (.on sprite (event {:down "pointerdown"
                        :over "pointerover"
                        :out "pointerout"})
         handler)))

(defn gen-sprite [scene & {:keys [key x y]}]
  (let [sprite (.sprite (-> scene .-physics .-add) x y key)]
    (.setCollideWorldBounds sprite true)
    {:sprite sprite
     :scene scene}))

(defn gen-no-display-sprite [scene & {:keys [x y width height]}]
  (let [no-display-sprite (.zone (-> scene .-add) x, y, width, height)]
    (.existing (-> scene .-physics .-add)
               no-display-sprite
               false)
    {:sprite no-display-sprite
     :scene scene}))

(defn gen-container [scene & {:keys [x y]}]
  {:sprite (.container (.-add scene) x, y)
   :scene scene})