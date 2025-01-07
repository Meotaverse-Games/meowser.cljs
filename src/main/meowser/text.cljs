(ns meowser.text)

(def default-font (atom nil))

(defn set-default-font! [font]
  (reset! default-font font))

(defn draw [& {:keys [scene sprite text x y color size font] :or {size 10 color "#ffffff"}}]
  (let [text
        (doto (.text (.-add scene) x y text
                     (clj->js {:color color
                               :fontSize (str size "px")
                               :fontFamily (or font @default-font)
                               :align "center"}))
          (.setOrigin 0.5, 0.5))]
    (when sprite
      (.add sprite text))
    text))