(ns meowser.button
  (:require [meowser.core :refer [MeowserBase scene sprite]]))

(def default-font (atom nil))

(defn set-default-font! [font]
  (reset! default-font font))

(defrecord MeowserButton [container]
  MeowserBase
  (scene [this] (.-scene this))
  (sprite [_this] container))

(defn draw
  [target & {:keys [x y width height color label label-color font-size on-click font padding]
             :or {width 100 height 40 color 0x0077ff label "" label-color "#fff" font-size 16}}]
  (let [scene (scene target)
        container (.container (.-add scene) x y)
        bg (doto (.rectangle (.-add scene) 0 0 width height color)
             (.setOrigin 0)
             (.setInteractive)
             (.on "pointerdown" (fn [] (when on-click (on-click)))))
        text (doto (.text (.-add scene) (/ width 2) (/ height 2) label
                          #js {:color label-color
                               :fontSize (str font-size "px")
                               :fontFamily (or font @default-font)
                               :align "center"})
               (.setOrigin 0.5))] 
    (.add container bg)
    (.add container text) 
    (.add (or (sprite target) (.-children scene)) container) 
    (->MeowserButton container)))
