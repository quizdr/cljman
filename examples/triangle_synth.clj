
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns triangle-synth
  (:use cljman.rib
        cljman.renderers
        cljman.synth))

;; this not working yet

(defn triangles []
  (display "triangles.tiff" :framebuffer :rgb)
  (image-format 500 500 1)
  (projection :perspective :fov [35])
  (translate 0 0 4)
  (rotate 200 0 1 0)
  (world
   (light-source :pointlight :from [1 2 4] :intensity 16)
   (light-source :ambientlight :intensity 0.2)
   (surface :plastic)
   (color :red)
   (displacement :dented)
                                        ;(let [tip (sweep [0 -1 -0.5] [0 1 1] 10)]
                                        ; (tri [-1 0 0]
                                        ;          [1 0 0]
                                        ;         tip))
   (pnt (sweep [-1 -1 1] [1 1 -1] 10) 0.1)))

(render :pixie triangles)