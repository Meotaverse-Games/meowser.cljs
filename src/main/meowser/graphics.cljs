(ns meowser.graphics
  (:require [meowser.core :as m.core]))

(defn line-style [graphics width color & [alpha]]
  (.lineStyle graphics width, color, (or alpha 1)))

(defn fill-style [graphics color & [alpha]]
  (.fillStyle graphics color, (or alpha 1.0)))

(defn circle [graphics & {:keys [x y radius]}]
  (.strokeCircle graphics x, y, radius))

(defn fill-circle [graphics & {:keys [x y radius]}]
  (.fillCircle graphics x, y, radius))

(defn rounded-rect [graphics & {:keys [x y width height radius]}]
  (.strokeRoundedRect graphics x, y width, height radius))

(defn fill-rounded-rect [graphics & {:keys [x y width height radius]}]
  (.fillRoundedRect graphics x, y width, height radius))

(defn rect [graphics & {:keys [x y width height]}]
  (.strokeRect graphics x, y width, height))

(defn fill-rect [graphics & {:keys [x y width height]}]
  (.fillRect graphics x, y width, height))

(defn with-graphics
  ([target body-fn]
   (with-graphics target {} body-fn))
  ([target opts body-fn]
   (let [^js/Phaser.Scene scene (m.core/scene target)
         sprite (m.core/sprite target)
         graphics (.graphics (.-make scene) (clj->js {:add (not (= (:type opts) :mask))}))]
     (body-fn graphics)
     (condp = (:type opts)
       :texture
       (let [{:keys [width height key]} opts]
         (doto graphics
           (.generateTexture key width height)
           (.destroy)))
       :mask
       (.setMask sprite (.createGeometryMask graphics))

       (when sprite
         (.add sprite graphics))))))