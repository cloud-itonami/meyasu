(ns meyasu.test-runner
  (:require [clojure.test :as t]
            [meyasu.methods.test-agent]
            [meyasu.methods.test-autorun]
            [meyasu.methods.test-pipeline-cid]
            [meyasu.viz.test-build-viz]))

(def suites
  '[meyasu.methods.test-agent
    meyasu.methods.test-autorun
    meyasu.methods.test-pipeline-cid
    meyasu.viz.test-build-viz])

(defn -main [& _]
  (let [{:keys [fail error]} (apply t/run-tests suites)]
    (when (pos? (+ fail error))
      (System/exit 1))))
