(ns treasurer-server.test-runner
  (:require [cljs.test :as test]
            [doo.runner :refer-macros [doo-tests]]
            [treasurer-server.core-test]))

(doo-tests 'treasurer-server.core-test)
