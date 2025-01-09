(ns meowser.debug)

(def state (atom {}))

(defn set-main-scene! [scene]
  (swap! state assoc :main-scene scene))

(defn main-scene []
  (:main-scene @state))

(defn set-player! [player]
  (swap! state assoc :player player))

(defn player []
  (:player @state))

(defn set-target-sprite! [sprite]
  (swap! state assoc :target-sprite sprite))

(defn target-sprite []
  (:target-sprite @state))

(defn inspect-target-sprite []
  {:type (-> (target-sprite) :sprite .-type)})
