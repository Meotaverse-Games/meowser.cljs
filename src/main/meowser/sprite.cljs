(ns meowser.sprite)

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

(defn set-offset! [{:keys [sprite]} x y]
  (.setOffset sprite x y))

(defn set-origin! [{:keys [sprite]} x y]
  (.setOrigin sprite x y))

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

(defn gen-sprite [scene & {:keys [key x y]}]
  (let [sprite (.sprite (-> scene .-physics .-add) x y key)]
    (.setCollideWorldBounds sprite true)
    {:sprite sprite
     :scene scene}))

(defn gen-frame-index-sprite [scene & {:keys [key frame-index x y]}]
  (let [sprite (.sprite (.-add scene) x y key frame-index)]
    {:sprite sprite
     :scene scene}))

(defn gen-no-display-sprite [^js/Phaser.Scene scene & {:keys [x y width height]}]
  (let [no-display-sprite (.zone (-> scene .-add) x, y, width, height)]
    (.existing (-> scene .-physics .-add)
               no-display-sprite
               false)
    {:sprite no-display-sprite
     :scene scene}))

(defn gen-container [^js/Phaser.Scene scene-or-sprite & {:keys [x y]}]
  (if (:sprite scene-or-sprite)
    (let [{:keys [^js/Phaser.Scene scene sprite]} scene-or-sprite
          container {:sprite (.container (.-add scene) x, y)
                     :scene scene-or-sprite}]
      (.add sprite (:sprite container))
      container)
    {:sprite (.container (.-add scene-or-sprite) x, y)
     :scene scene-or-sprite}))
