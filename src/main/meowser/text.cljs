(ns meowser.text
  (:require [meowser.core :refer [MeowserBase scene sprite]]))

(defrecord MeowserText [text]
  MeowserBase
  (scene [this] (.-scene this))
  (sprite [this] text))

(def default-font (atom nil))

(defn set-default-font! [font]
  (reset! default-font font))

(defn set-color! [{:keys [text]} color]
  (.setColor text color))

(defn draw [target & {:keys [text x y color size font padding]
                      :or {color "#ffffff" size 10}}]
  (let [scene (scene target)
        text
        (doto (.text (.-add scene) x y text
                     (clj->js {:color color
                               :fontSize (str size "px")
                               :fontFamily (or font @default-font)
                               :align "center"
                               :lineSpacing 100}))
          (.setOrigin 0.5))]
    (when padding
      (.setPadding text (clj->js padding)))
    (when (sprite target)
      (.add (sprite target) text))
    (->MeowserText text)))
