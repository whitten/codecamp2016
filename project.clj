(defproject de.blubberquark/codecamp2016 "1.0.0-SNAPSHOT"
  :description "codecamp"
  :dependencies [[clj-http-lite "0.3.0"]
                 [org.clojure/data.csv "0.1.3"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/clojure "1.7.0"]
                 [net.sf.extjwnl/extjwnl "1.9.1"]
                 [net.sf.extjwnl/extjwnl-data-wn31 "1.2"]
                 [org.boofcv/all "0.23"]
                 [gov.nih.imagej/imagej "1.47"]]
  :min-lein-version  "2.0.0"
  :source-paths      ["src-clojure"]
  :java-source-paths ["src"])
