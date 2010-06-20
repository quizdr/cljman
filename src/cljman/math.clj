
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns cljman.math
  (:refer-clojure :rename {+ add, - sub, * mul, / div})
  (:require [clojure.contrib.math :as math])
  (:require [clojure.contrib.generic :as generic])
  (:use (clojure.contrib.generic arithmetic)))

;; this is ugly. it should be possible to specify this
;; behaviour without having to refer to implementation
;; types, yuck
(derive clojure.lang.ISeq ::sequential)
(derive clojure.lang.PersistentVector ::sequential)
(derive ::sequential generic/root-type)

;; From clojure.contrib.generic.arithmetic docs
;; This library defines generic versions of + - * / as multimethods
;; that can be defined for any type. The minimal required 
;; implementations for a type are binary + and * plus unary - and /.
;; Everything else is derived from these automatically. Explicit
;; binary definitions for - and / can be provided for
;; efficiency reasons.

(defn vx [v] (first v))
(defn vy [v] (second v))
(defn vz [v] (nth v 2))
(defn vw [v] (nth v 3))

(defmethod + [Number Number] [x y]
  (add x y))

(defmethod + [::sequential ::sequential] [v0 v1]
  (map add v0 v1))


(defmethod - Number [s]
  (sub s))

(defmethod - [Number Number] [x y]
  (sub x y))

(defmethod - ::sequential [v]
  (map sub v))

(defmethod - [::sequential ::sequential] [v0 v1]
  (map sub v0 v1))


(defmethod * [Number Number] [x y]
  (mul x y))

(defmethod * [::sequential ::sequential] [v0 v1]
  (map mul v0 v1))

(defmethod * [::sequential Number] [v s]
  (map (partial mul s) v))

(defmethod * [Number ::sequential] [s v]
  (map (partial mul s) v))


(defmethod / Number [s]
  (div s))

(defmethod / [Number Number] [x y]
  (div x y))

(defmethod / [::sequential ::sequential] [v0 v1]
  (map div v0 v1))

(defmethod / [::sequential Number] [v s]
  (map #(div % s) v))

(defn squared [s] (mul s s))

(defn cubed [s] (mul s s s))

(def pi Math/PI)

(let [pi-over (div pi 180)
      over-pi (div 1 pi-over)]
  (defn deg2rad [d]
    (mul d pi-over))
  (defn rad2deg [r]
    (mul r over-pi)))

(defn magnitude-squared
  "Returns the magnitude squared of the vector v.
   This is useful when you need to compare magnitudes
   as it avoids a square root computation. See also magnitude="
  [v]
  (apply add (map squared v)))

(defn magnitude
  "Returns the magnitude of vector v."
  [v]
  ;; note: double called to avoid exact math in math/sqrt
  (math/sqrt (apply add (map (comp double squared) v))))

(defn magnitude=
  "Returns whether two vectors have the same magnitude."
  [v0 v1]
  (= (magnitude-squared v0)
     (magnitude-squared v1)))

(defn manhattan-magnitude
  "Returns the l1 norm on vector v, i.e. the sum of the
   absolute values of the components."
  [v]
  (apply add (map math/abs v)))

(defn normalize
  "Returns a normalized version of passed vector or sequence"
  [v]
  (/ v (magnitude v)))

;; just for convenience, nary-dispatch does the job
(defmulti distance
  "Computes the distance between two points or two scalars."
  generic/nary-dispatch)

(defmethod distance [Number Number] [x y]
  (math/abs (sub x y)))

(defmethod distance [::sequential ::sequential] [v0 v1]
  (magnitude (sub v0 v1)))

(defn dot
  "Computes the dot-product or inner-product, v0 . v1"
  [v0 v1]
  (apply add (* v0 v1)))

(defn cross
  "Computes the cross-product v0 x v1"
  [v0 v1]
  (let [[x0 y0 z0] v0
        [x1 y1 z1] v1]
    [(sub (mul y0 z1) (mul z0 y1))
     (sub (mul z0 x1) (mul x0 z1))
     (sub (mul x0 y1) (mul y0 x1))]))

(defn vtheta
  "Computes the angle between two vectors. Note that if
   you have two normalized vectors you should call nvtheta."
  [v0 v1]
  (Math/acos (div (dot v0 v1)
                  (mul (magnitude v0)
                       (magnitude v1)))))

(defn nvtheta
  "Computes the angle between two normalized vectors. Note
   that vtheta will compute the angle between any two arbitrary
   vectors."
  [nv0 nv1]
  (Math/acos (dot nv0 nv1)))

(defn vproj
  "Takes two vectors and computes the projection of
   the source (src) vector onto the onto vector.

            ^
           /
    src  /
       /
     /
   /        (vproj src onto)
  ------>...>
   onto

   For the vector perpendicular to vproj, pointing
   up to the head of src, see vproj-perp, or just write:
   (- src (vproj src onto))."
  [src onto]
  (let [onto-mag (magnitude onto)]
    (* onto (if (= 1 onto-mag)
              (dot src onto)
              (div (dot src onto)
                   (squared onto-mag))))))

(defn vproj-perp
  "Takes two vectors and computes the projection of
   the source (src) vector onto the onto vector.
   Then returns the perpendicular to the projection.

            ^
           /|
    src  /  |
       /    | (vproj-perp src onto)
     /      |
   /        |    
  ------>...> vproj
   onto

  See also vproj, for just the projection component."
  [src onto]
  (- src (vproj src onto)))




