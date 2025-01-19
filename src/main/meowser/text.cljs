(ns meowser.text)

(def default-font (atom nil))

(defn set-default-font! [font]
  (reset! default-font font))

(defn draw [{:keys [scene sprite] :as scene-or-sprite}
            & {:keys [text x y color size font padding]
               :or {color "#ffffff" size 10}}]
  (let [scene (or scene scene-or-sprite)
        text
        (doto (.text (.-add scene) x y text
                     (clj->js {:color color
                               :fontSize (str size "px")
                               :fontFamily (or font @default-font)
                               :align "center"
                               :lineSpacing 100}))
          (.setOrigin 0.5, 0.5))]
    (when padding
      (.setPadding text (clj->js padding)))
    (when sprite
      (.add sprite text))
    text))