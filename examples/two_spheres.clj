
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns two-spheres
  (:use [cljman rib renderers]
        cljman.rib.util))

(defn rib []

  (display "two-spheres" :framebuffer :rgb)
  (image-format 640 500 1)
  (projection :perspective :fov [26])

  (translate 0 0 6)
  
  (world
   (ambient-light 0.4)
   (distant-light [8 8 -3] [0 0 0] 3)

   (color :blue-violet)
   (surface :plastic)
   (translate -0.2 0 0)
   (opacity 0.28)
   (sphere 1 -1 1 360)

   (color :gold)
   (surface :metal)
   (translate 1 0 2)
   (opacity 0.35)
   (sphere 1 -1 1 360)))

(render :pixie rib)

