
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns cljman.renderers.delight
  (:require [cljman.process :as proc]))

(def info
     {:cmnd-names {:renderer "renderdl"
                   :shader-compiler "shaderdl"}
      
      :renderer-options {:framebuffer "-d"
                         :framebuffer-close "-D"
                         :threads #(str "-t:" ({:all 0} % %))
                         :processes #(str "-P " ({:all 0} % %))
                         :hosts #(apply str "-hosts " (interpose "," %))
                         :tiling #(str "-tiling " ({:balanced "b"
                                                    :mixed "m"
                                                    :vertical "v"
                                                    :horizontal "h"} % %))
                         :ssh "-ssh"
                         :job-script #(str "-jobscript " %)
                         :job-script-param #(str "-jobscriptparam " %)
                         :i-display "-id"
                         :ignore-framebuffer "-nd"
                         :resolution #(apply str "-res " %)
                         :frames #(apply proc/spaced-str "-frames" %)
                         :crop #(apply proc/spaced-str "-crop" %)
                         :no-init "-noinit"
                         :stats #(str "-stats" ({:low 1 :medium 2 :high 3} % %))
                         :stats-file #(str "-statsfile " %)
                         :max-messages #(str "-maxmessages " %)
                         :filter-messages #(apply str
                                                  "-filtermessages "
                                                  (interpose "," %))
                         :beep "-beep"
                         :beeps "-beeps"}

      :shader-compiler-options {}})
