(ns meowser.textures
  (:require [meowser.debug :as d]))

(defn dot-circle [scene key color radius dot-size]
  (let [graphics (-> scene .-add (.graphics))]
    (doto graphics
      (.fillStyle color))
    (doseq [y (range (- radius) (inc radius))]
      (doseq [x (range (- radius) (inc radius))]
        (when (<= (+ (* x x) (* y y)) (* radius radius))
          (.fillRect graphics
                     (+ (/ radius 2) (* x 1))
                     (+ (/ radius 2) (* y 1))
                     dot-size dot-size))))
    (doto graphics
      (.generateTexture key (* radius 4) (* radius 4))
      (.destroy))))

(dot-circle @d/main-scene "hoge" 0xffffff 30 5)

(.image (.-add @d/main-scene) 300, 300, "hoge")


;; var CreateTexture0 = function (scene, key) {
;;     // width: 20-0-20
;;     // height: 100-0-100
;;     var width = 40, height = 200;
;;     scene.add.graphics()
;;         .fillStyle(COLOR_PRIMARY)
;;         .fillEllipse(width / 2, height / 2, width, height)
;;         .generateTexture(key, width, height)
;;         .destroy();
;; }