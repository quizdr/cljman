
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns microbes
  (:use cljman.rib cljman.util cljman.rib.util cljman.renderers))

;; be sure to run this one from the examples directory
;; and that you have compiled the electron_mic shader

(defn microbe [freq disp clr]
  (color clr)
  (displacement-bound disp)  
  (displacement "micro_bumps" "float Kd" [disp] "float mult" [freq])
  (surface "electron_mic" "float Kd" [2.0])
  (sphere 1 -1 1 360))

(defn rib []
  (option "limits" "bucketsize" [32 32])
  (perspective-projection 30)
  (clipping 1 10)

  (translate 0 0 5)
  
  (world   
   (dotimes [_ 10]
     (transform
      (apply rotate (rand 360) (rand-vec))
      (translate 0 0 (rand-between 0.3 1.3))
      (apply scale (take 3 (repeatedly #(rand-between 0.3 0.9))))
      (let [freq (rand-between 0.1 1.5)
            disp (rand-between 0.2 0.8) 
            clr [0.7 0.75 (rand-between 0.6 0.8)]]
        (microbe freq disp clr))))))

(prn (render :aqsis (preview-fn rib :medium)))
