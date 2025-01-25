(ns meowser.tween
  (:require [meowser.core :as m.core]))

(defrecord MeowserTween [tween]
  m.core/MeowserBase
  (scene [this] (.-scene this))
  (sprite [this] nil))

(defn gen-tween [target-scene targets-to-be-tweened & {:keys [alpha duration ease]}]
  (->MeowserTween
   (.add (.-tweens (m.core/scene target-scene))
         (clj->js {:targets targets-to-be-tweened
                   :alpha alpha
                   :duration duration
                   :ease ease}))))
