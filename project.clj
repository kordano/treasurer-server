(defproject treasurer-server "0.0.1-SNAPSHOT"
  :description "Small finance management server"
  
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48"]]
  
  :profiles {:dev
             {:plugins [[lein-doo "0.1.4"]]}}
 
  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-doo "0.1.5"]
            [lein-npm "0.6.1"]]
 
  :npm {:dependencies [[source-map-support "0.2.8"]
                       [express "4.13.3"]]
        :package {:scripts {:start "node index.js"}}}
  
  :clean-targets ["dist"]
  
  :cljsbuild {:builds
              {:dev
               {:source-paths ["src"]
                :compiler {:main treasurer_server.core
                           :output-to "dist/treasurer.js"
                           :output-dir "dist/out"
                           :target :nodejs
                           :cache-analysis true
                           :optimizations :none
                           :source-map true}}
               :test
               {:source-paths ["src" "test"]
                :compiler {:output-to "dist/test/compiled.js"
                           :output-dir "dist/test"
                           :main treasurer-server.test-runner
                           :optimizations :none
                           :target :nodejs}}
               :prod
               {:source-paths ["src/treasurer_server"]
                :compiler {:main treasurer_server.core
                           :output-to "dist/bundle.js"
                           :target :nodejs
                           :cache-analysis true
                           :optimizations :advanced}}}}
  )
  
