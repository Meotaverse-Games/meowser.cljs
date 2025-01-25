(ns meowser.ninepatch
  (:require ["phaser3-rex-plugins/plugins/ninepatch.js" :as ninepatch]
            [meowser.core :refer [scene sprite]]
            [meowser.sprite :refer [->MeowserSprite]]))

(def NinePatch (get (js->clj ninepatch) "default"))

(defn resize [{:keys [^js/Phaser.Scene sprite]} width height]
  (.resize sprite width height))

(defn- array-fill-undefined! [array]
  (doseq [i (range (count array))]
    (when (nil? (aget array i))
      (aset array i js/undefined)))
  array)

(defn gen-ninepatch [target & {:keys [x y width height key base-frame columns rows]}]
  (->MeowserSprite (doto (NinePatch. (scene target)
                                     x y
                                     width height
                                     key base-frame
                                     (array-fill-undefined! columns) (array-fill-undefined! rows))
                     (->> (.add (sprite target))))))
 