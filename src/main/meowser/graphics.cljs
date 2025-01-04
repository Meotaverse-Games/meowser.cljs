(ns meowser.graphics)

(defn circle [scene & {:keys [x y radius color]}]
  (.circle (.-add scene) x, y, radius, color))
