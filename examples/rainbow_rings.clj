
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns rainbow-rings
  (:use cljman.rib
        cljman.renderers))

(defn rib []
  (display "rainbow-rings.tiff" :framebuffer :rgb)
  (image-format 600 600 1)
  (projection :perspective :fov 45)

  (translate 2 0 20)
  (rotate -60 0 1 1)
  
  (world
   (dotimes [n 8]
     (translate 0 0 5)
     (rotate 45 1 0 0)
     (translate 0 0 -5)
     (dotimes [_ 36]
       (apply color (take 3 (repeatedly rand)))
       (transform
        (translate 0 0 2)
        (sphere 1.0 -0.3 0.3 120))
       (rotate 10 0 1 0)))))

(render :pixie rib)