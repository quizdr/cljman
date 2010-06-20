
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns cljman.renderers.aqsis
  (:require [cljman.process :as proc]))

(def rib-options :TODO)

(def info
     {:cmnd-names {:renderer "aqsis"
                   :shader-compiler "aqsl"}
      
      :renderer-options {:framebuffer "-d"
                         :no-color "-nc"
                         :crop #(apply proc/spaced-str "-crop" %)
                         :end-of-frame #(str "-endofframe=" %)
                         :no-standard "-nostandard"
                         :verbosity #(str "-verbose="
                                          ({:errors 0 :warnings 1
                                            :info 2 :debug 3} % %))
                         :priority #(str "-priority="
                                         ({:idle 0 :normal 1 :high 2 :rt 3} % %))
                         :type #(str "-type=" %)
                         :add-type #(str "-addtype=" %)
                         :mode #(str "-mode=" %)
                         :frames #(apply proc/spaced-str "-frames" %)
                         :frames-list #(str "-frameslist=" %)
                         :beep "-beep"
                         :resolution #(apply proc/spaced-str "-res" %)
                         ; :rib-options #(rib-options %)
                         :shaders #(str "-shaders=" %)
                         :archives #(str "-archives=" %)
                         :textures #(str "-textures=" %)
                         :displays #(str "-displays=" %)
                         :procedurals #(str "-procedurals=" %)}
      
      :shader-compiler-options {}})



