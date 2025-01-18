(ns meowser.tilemap)

(defn add-tileset-image! [{:keys [map]} tileset-name key]
  (.addTilesetImage map (name tileset-name) key))

(defn create-layer! [{:keys [map scale]} layer-name tilesets {:keys [offset] :or {offset [0 0]}}]
  (let [[offset-x offset-y] offset]
    {:layer (.createLayer map (name layer-name)
                          (clj->js tilesets)
                          offset-x offset-y)
     :offset [offset-x, offset-y]
     :scale scale}))
