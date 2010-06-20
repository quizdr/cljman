
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns print-simple-rib
  (:use cljman.rib))

(defn rib []
  (display "simple-sphere.tiff" :framebuffer :rgb)
  (image-format 640 500 1)
  (projection :perspective :fov 26)
  (translate 0 0 6)
  (world
   (color :red)
   (translate -0.2 0 0)
   (sphere 1 -1 1 360)))

(rib)
