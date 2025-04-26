(ns core
  (:import [browser Browser Callback]
           [org.graalvm.webimage.api JSObject])
  (:gen-class))

(defn as-callback [f]
  (reify Callback
    (run [this value]
      (f value))))

(defn invoke-method [^JSObject object ^String method & args]
  (.call ^JSObject (.get object method) object (object-array args)))

(defn -main [& args]
 (Browser/main)
 (let [window (Browser/globalThis)
       root (Browser/querySelector "#btn-root")
       button (Browser/createElement "button")
       on-click (fn [event]
                  (invoke-method window "alert" "Hello, from Clojure in WASM!"))]
   (.set button "textContent" "Press me")
   (Browser/addEventListener button "click" (as-callback on-click))
   (Browser/appendChild root button)))