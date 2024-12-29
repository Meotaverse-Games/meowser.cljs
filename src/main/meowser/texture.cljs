(ns meowser.texture)

(defn dot-circle [scene key color radius dot-size]
  (let [graphics (-> scene .-add (.graphics))]
    (doto graphics
      (.fillStyle color))
    (doseq [y (range (- radius) (inc radius))]
      (doseq [x (range (- radius) (inc radius))]
        (when (< (+ (* x x) (* y y)) (* radius radius))
          (.fillRect graphics
                     (int (* dot-size (+ radius x)))
                     (int  (* dot-size (+ radius y)))
                     dot-size dot-size))))
    (doto graphics
      (.generateTexture key (* radius 2 dot-size) (* radius 2 dot-size))
      (.destroy))))
