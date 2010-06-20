
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns motion-path
  (:use cljman.rib
        cljman.renderers))

(defn rib []
  (display "motion-path.tiff" :framebuffer :rgb)
  (projection :perspective :fov 30)
  (image-format 300 300 1)
  (pixel-samples 5 5)
  
  (shutter 0 1)
  (translate 0 0 5)

  (world
   (light-source :ambientlight :intensity 0.1)
   (light-source :pointlight :from [-2 4 -2] :intensity 10)

   (motion [0 0.5 1]
    (translate -0.5 0 0)
    (translate 0 0.5 0)
    (translate 0.5 0 0))

   (color :red)
   (surface :plastic)
   (sphere 1 -1 1 360)))

(render :pixie rib)