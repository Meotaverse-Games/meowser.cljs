(ns frameworks.phaser.input
  (:require ["phaser" :as phaser]
            [frameworks.phaser.debug :as pd]))

(def key-codes (-> phaser .-Input .-Keyboard .-KeyCodes))

(def direction #{:right :down :left :up :cancel})

(def movable-key-code->direction {"arrowright" :right
                                  "arrowdown" :down
                                  "arrowleft" :left
                                  "arrowup" :up})

(require '[frameworks.phaser.debug :as pd])

(.on (-> @pd/main-scene .-input .-keyboard)
     "keydown"
     (fn [key]
       (if-let [direction (-> key .-code .toLowerCase movable-key-code->direction)]
         (handle-)
         (prn code))))

(defn set-key-events [scene codes])

;; export enum MessageEventKey {
;;   Next,
;;   Prev,
;; }

;;       const { keyboard } = scene.input;
;;       for (const key of [keyCodes.SPACE, keyCodes.ENTER, keyCodes.LEFT]) {
;;         this.justKeys.set(key, keyboard!.addKey(key, false)!);
;;       }

;;       this.setOnKeyEvent(scene, "keydown", (code) => {
;;         const direction = movableKeyCodes2direction.get(code);
;;         if (direction !== undefined && this.moveEventHandler !== undefined) {
;;           this.moveEventHandler(direction);
;;         }
;;       });
;;       this.setOnKeyEvent(scene, "keyup", (_) => {
;;         if (this.moveEventHandler !== undefined) {
;;           this.moveEventHandler(Direction.cancel);
;;         }
;;       });
;;     }

;;     this.sceneManager = sceneManager;
;;   }

;;   setMoveEventHandler(moveEventHandler: MoveEventHandler) {
;;     this.moveEventHandler = moveEventHandler;
;;   }

;;   setMessageEventHandler(messageEventHandler: MessageEventHandler) {
;;     this.messageEventHandler = messageEventHandler;
;;   }

;;   setOnKeyEvent(
;;     scene: Phaser.Scene,
;;     event: string,
;;     fn: (code: string) => void
;;   ) {
;;     scene.input.keyboard?.on(event, (key: { code: string }) => {
;;       fn(key.code.toLowerCase());
;;     });
;;   }

;;   update() {
;;     for (const [key, val] of this.justKeys.entries()) {
;;       if (
;;         this.messageEventHandler !== undefined &&
;;         Phaser.Input.Keyboard.JustDown(val) &&
;;         this.sceneManager.currentEvent()?.type === EventType.message
;;       ) {
;;         const keyCodes = Phaser.Input.Keyboard.KeyCodes;
;;         switch (key) {
;;           case keyCodes.ENTER:
;;           case keyCodes.SPACE:
;;             this.messageEventHandler(MessageEventKey.Next);
;;             break;

;;           case keyCodes.LEFT:
;;             this.messageEventHandler(MessageEventKey.Prev);
;;             break;
;;         }
;;       }
;;     }
;;   }
;; }
