
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns cljman.renderers.pixie)

(def info
     {:cmnd-names {:renderer "rndr"
                   :shader-compiler "sdrc"}
      
      :renderer-options {:framebuffer "-d"
                         :range #(str "-f " %)
                         :quiet "-q"
                         :statistics "-t"
                         ;; this breaks the pipe for some reason
                         ;; :subprocesses #(str "-P:" %)
                         :threads #(str "-t:" %)
                         :client #(str "-c " %)
                         :server #(str "-s " %)}
      
      :shader-compiler-options {}})

