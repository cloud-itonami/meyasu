(require '[clojure.edn :as edn]
         '[clojure.java.io :as io]
         '[clojure.string :as str]
         '[cheshire.core :as json])

(def root (.getCanonicalFile (io/file ".")))
(def files (->> (file-seq root)
                (remove #(.isDirectory %))
                (remove #(str/includes? (.getPath %) "/.git/"))))
(def rel #(.toString (.relativize (.toPath root) (.toPath %))))

(doseq [f (filter #(str/ends-with? (.getName %) ".edn") files)]
  (try (edn/read-string (slurp f))
       (catch Exception e
         (throw (ex-info "invalid canonical EDN" {:file (rel f)} e)))))

(when-not (= (edn/read-string (slurp "data/seed.edn"))
             (json/parse-string (slurp "wire/seed.json")))
  (throw (ex-info "canonical EDN seed differs from wire JSON snapshot" {})))

(let [forbidden (filter #(re-find #"(?:^|/)(?:go\.mod|go\.sum|run_tests\.sh|deploy\.sh|[^/]+\.(?:go|py))$" (rel %)) files)
      misplaced (filter #(and (re-find #"\.(?:json|jsonld|jsonl)$" (rel %))
                              (not (str/starts-with? (rel %) "wire/"))) files)]
  (when (seq forbidden)
    (throw (ex-info "deprecated implementation artifacts remain" {:files (mapv rel forbidden)})))
  (when (seq misplaced)
    (throw (ex-info "serialized artifacts must live under wire/" {:files (mapv rel misplaced)}))))

(println "audit: ok")
