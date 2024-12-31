(ns meowser.layer)

(defn set-collision-from-collision-group! [{:keys [layer]}]
  (.setCollisionFromCollisionGroup layer true true))

(defn set-scale! [{:keys [layer]} scale]
  (.setScale layer scale))

(defn draw-collision-groups [{:keys [layer scale]}]
  (prn [layer scale])
  (let [graphics (.graphics (.-add (.-scene layer)))]
    (.forEachTile
     layer
     (fn [tile]
       (let [tile-world-pos (.tileToWorldXY layer (.-x tile) (.-y tile))
             [tileset] (-> tile .-tilemapLayer .-tileset)
             collision-group (.getTileCollisionGroup tileset (.-index tile))]
         (when (and collision-group (> (count (.-objects collision-group)) 0))
           (if (and (.-properties collision-group)
                    (-> collision-group .-properties .-isInteractive))
             (.lineStyle graphics 1, 0x00ff00)
             (.lineStyle graphics 1, 0x00ffff))
           (doseq [object (.-objects collision-group)]
             (let [x (+ (.-x tile-world-pos) (* (.-x object) scale))
                   y (+ (.-y tile-world-pos) (* (.-y object) scale))]
               (cond
                 (.-rectangle object) (.strokeRect graphics x, y, (* (.-width object) scale) (* (.-height object) scale)))))))))))

(defn collider-with-sprite [{:keys [layer]} {:keys [sprite]} & [collide-fn]]
  (.collider (-> layer .-scene .-physics .-add) sprite layer collide-fn))
