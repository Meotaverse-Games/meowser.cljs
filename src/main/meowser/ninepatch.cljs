(ns meowser.ninepatch
  (:require ["phaser3-rex-plugins/plugins/ninepatch.js" :as ninepatch]))

(def NinePatch (get (js->clj ninepatch) "default"))

(defn gen-nine-patch [scene sprite & {:keys [x y width height key base-frame columns rows]}]
  {:sprite (doto (NinePatch. scene x y width height key base-frame columns (clj->js rows))
             (->> (.add sprite)))
   :scene scene})