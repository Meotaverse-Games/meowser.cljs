(ns meowser.graphics
  (:require
   [meowser.debug :as m.debug]))

(defn circle [graphics & {:keys [x y radius]}]
  (.strokeCircle graphics x, y, radius))

(defn line-style [graphics width color alpha]
  (.lineStyle graphics width, color, alpha))

(defn fill-style [graphics color alpha]
  (.fillStyle graphics color, alpha))

(defn rounded-rect [graphics & {:keys [x y width height radius]}]
  (.strokeRoundedRect graphics x, y width, height radius))

(defn fill-rounded-rect [graphics & {:keys [x y width height radius]}]
  (.fillRoundedRect graphics x, y width, height radius))

(defn rect [graphics & {:keys [x y width height]}]
  (.strokeRect graphics x, y width, height))

(defn fill-rect [graphics & {:keys [x y width height]}]
  (prn [:fill-rect x, y, width, height])
  (.fillRect graphics x, y width, height))

(defn with-graphics
  ([sprite-or-scene body-fn]
   (with-graphics sprite-or-scene nil body-fn))
  ([sprite-or-scene texture-opts body-fn]
   (let [scene (or (:scene sprite-or-scene) sprite-or-scene)
         sprite (:sprite sprite-or-scene)
         graphics (.graphics (.-add scene))]
     (body-fn graphics)
     (if texture-opts
       (let [{:keys [width height key]} texture-opts]
         (doto graphics
           (.generateTexture key width height)
           (.destroy)))
       (.add sprite graphics)))))