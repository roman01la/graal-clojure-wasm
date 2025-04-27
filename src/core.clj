(ns core
  (:import [browser Browser Callback]
           [org.graalvm.webimage.api JSObject])
  (:gen-class))

;; Interop, DOM

(defn as-callback [f]
  (reify Callback
    (run [this value]
      (f value))))

(defn invoke-method [^JSObject object ^String method & args]
  (.call ^JSObject (.get object method) object (object-array args)))

(defn render-dom []
 (Browser/main)
 (let [window (Browser/globalThis)
       root (Browser/querySelector "#btn-root")
       button (Browser/createElement "button")
       on-click (fn [event]
                  (invoke-method window "alert" "Hello, from Clojure in WASM!"))]
   (.set button "textContent" "Press me")
   (Browser/addEventListener button "click" (as-callback on-click))
   (Browser/appendChild root button)))

;; Benchmark

(def nanosecond 1)
(def microsecond (* nanosecond 1000))
(def millisecond (* microsecond 1000))
(def second_ (* millisecond 1000))

(defn format-duration
  "format-duration turns nanosecond durations, such as those provided
  by bench and converts them to a microsecond, millisecond, or second
  value (ie. 32ns, 43.2µs, 54.7ms, 3.1s)."
  [ns]
  (cond
    (>= ns second_),,,, (format (str "%.1fs") (float (/ ns second_)))
    (>= ns millisecond) (format (str "%.1fms") (float (/ ns millisecond)))
    (>= ns microsecond) (format (str "%.1fµs") (float (/ ns microsecond)))
    :else,,,,,,,,,,,,,, (format (str "%dns") ns)))

(defn ^:private format-order-of-magnitude [ops]
  (as-> (str ops) $
        (filter #{\0} $)
        (count $)
        (format "10**%d" $)))

(def max-ops 10000000)
(def max-duration second_)

(defmacro ^:private time-ns [ex]
  `(let [start# (. System (nanoTime))]
     (do ~ex (- (. System (nanoTime)) start#))))

(defn time-it [f n]
  (time-ns (dotimes [_ n] (f))))

(defn bench
  "bench receives a function `f` and an integer `n`.
  It runs `f` `n` times and reports the total elapsed
  time as well as the per-operation average time elapsed.
  It is intended to be called by a higher-level function
  that will pass in successively higher values of `n`
  until a time threshold is crossed."
  [f n]
  (let [elapsed (time-it f n)
        average (quot elapsed n)]
    {:total-ops n
     :total-ns  elapsed
     :per-op-ns average}))

(defn- within-time-threshold [{:keys [total-ns]}] (< total-ns max-duration))
(defn- within-ops-threshold [{:keys [total-ops]}] (< total-ops max-ops))

(def ^:private within-thresholds
  (every-pred within-time-threshold
              within-ops-threshold))

(defn benchmark
  "benchmark passes the provided function `f` to bench
  with successively higher `n` values until it has either:
  1. exhausted a reasonable amount of time or
  2. measured enough executions to have an accurate
  per-operation average.
  It returns the low-level results of the last call to bench."
  [f]
  (let [by-powers-of-10 #(* 10 %)
        benchmark       #(bench f %)]
    (as-> (iterate by-powers-of-10 1) $
          (map benchmark $)
          (drop-while within-thresholds $)
          (first $))))

(defn report
  "Translates the result of benchmark into more human-readable values.
  Can be passed to println for a decent report."
  [f]
  (let [{:keys [total-ops total-ns per-op-ns]} (benchmark f)]
    {:total-ops   (format-order-of-magnitude total-ops)
     :total-time  (format-duration total-ns)
     :per-op-time (format-duration per-op-ns)}))

(defn run-benchmark []
  (println "- big reduce:        " (report #(reduce * (range 10000000))))
  (println "- int/float division:" (report #(int (/ 101 10))))
  (println "- float division:    " (report #(/ 101 10)))
  (println "- integer division:  " (report #(quot 101 10))))

(defn -main [& args]
  (println "Hello, World!")
  (render-dom)
  (.set (Browser/globalThis) "runBenchmark" (as-callback (fn [_] (run-benchmark)))))