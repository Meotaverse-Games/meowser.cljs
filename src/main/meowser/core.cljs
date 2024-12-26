(ns meowser.core
  (:require [integrant.core :as ig]
            ["phaser" :as phaser]))

(defn gen-game [{:keys [debug? width height scenes gravity] :or {debug? false}}]
  (let [config
        (clj->js {:type (.-WEBGL phaser)
                  :parent "root"
                  :width (.-innerWidth js/window)
                  :height (.-innerHeight js/window)
                  :pixelArt false
                  :physics {:default "arcade"
                            :arcade {:debug debug?
                                     :gravity {:y gravity}}}
                  :scale {:mode (-> phaser .-Scale .-ScaleModes .-FIT)
                          :autoCenter (-> phaser .-Scale .-CENTER_BOTH),
                          :width width,
                          :height height},
                  :scene (clj->js scenes)})]
    (phaser/Game. config)))

(defmethod ig/init-key :meowser/game [_ opts]
  (gen-game opts))
