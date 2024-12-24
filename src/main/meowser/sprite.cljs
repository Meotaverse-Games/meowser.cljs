(ns meowser.sprite
  (:require ["phaser" :as phaser]))

(defn flip-x! [{:keys [sprite]} flip-x?]
  (set! (.-flipX sprite) flip-x?))

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

(defn play-anim [{:keys [sprite]} name]
  (.play sprite (clj->js {:key name}) true))

(defn follow-camera [{:keys [scene sprite]}]
  (.startFollow (-> scene .-cameras .-main) sprite true))

(defn set-body-size! [{:keys [sprite]} width height]
  (.setBodySize sprite width height))

(defn set-offset! [{:keys [sprite]} x y]
  (.setOffset sprite x y))

(defn gen-sprite [scene & {:keys [key x y]}]
  (prn :hoge)
  (let [sprite (.sprite (-> scene .-physics .-add) x y key)]
    (.setCollideWorldBounds sprite true)
    {:sprite sprite
     :scene scene}))



