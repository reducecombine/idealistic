;; Please don't bump the library version by hand - use ci.release-workflow instead.
(defproject net.vemv/idealistic "unreleased"
  ;; Please keep the dependencies sorted a-z.
  :dependencies [[cheshire "5.8.1"]
                 [clj-http "3.10.0"]
                 [com.nedap.staffing-solutions/speced.def "1.1.0-alpha3"]
                 [com.nedap.staffing-solutions/utils.spec.predicates "1.0.0"]
                 [org.clojure/clojure "1.10.1"]
                 [throttler "1.0.0"]]

  :description "A real estate finder. Uses the Idealista API."

  :url "https://github.com/reducecombine/idealistic"

  :min-lein-version "2.0.0"

  :license {:name "EPL-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :target-path "target/%s"

  :test-paths ["src" "test"]

  :monkeypatch-clojure-test false

  :plugins [[lein-pprint "1.1.2"]]

  ;; A variety of common dependencies are bundled with `nedap/lein-template`.
  ;; They are divided into two categories:
  ;; * Dependencies that are possible or likely to be needed in all kind of production projects
  ;;   * The point is that when you realise you needed them, they are already in your classpath, avoiding interrupting your flow
  ;;   * After realising this, please move the dependency up to the top level.
  ;; * Genuinely dev-only dependencies allowing 'basic science'
  ;;   * e.g. criterium, deep-diff, clj-java-decompiler

  ;; NOTE: deps marked with #_"transitive" are there to satisfy the `:pedantic?` option.
  :profiles {:dev {:dependencies [[cider/cider-nrepl "0.16.0" #_"formatting-stack needs it"]
                                  [com.clojure-goes-fast/clj-java-decompiler "0.2.1"]
                                  [com.stuartsierra/component "0.4.0"]
                                  [com.taoensso/timbre "4.10.0"]
                                  [criterium "0.4.4"]
                                  [formatting-stack "1.0.0-alpha3"
                                   :exclusions [rewrite-clj]]
                                  [lambdaisland/deep-diff "0.0-29"]
                                  [medley "1.1.0"]
                                  [org.clojure/core.async "0.4.490"]
                                  [org.clojure/math.combinatorics "0.1.1"]
                                  [org.clojure/test.check "0.10.0-alpha3"]
                                  [org.clojure/tools.namespace "0.3.0-alpha4"]
                                  [org.clojure/tools.reader "1.1.1" #_"transitive"]
                                  [rewrite-clj "0.6.1" #_"transitive"]]
                   :plugins      [[lein-cloverage "1.1.1"]]
                   :source-paths ["dev" "test"]
                   :repl-options {:init-ns dev}}

             :ci  {:pedantic?   :abort
                   :jvm-opts    ["-Dclojure.main.report=stderr"]
                   ;; `ci.release-workflow` relies on runtime assertions
                   :global-vars {*assert* true}}})
