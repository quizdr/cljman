
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns cljman.util
  (:require [clojure.contrib.math :as math]))

(defn frange
  "Returns a sequence of count steps of floats from
   start to end, both inclusive. If called with only
   two arguments, then starts from 0.0"
  ([start end steps]
     (let [dec (/ (- start end) (- steps 1))]
       (map #(- start (* % dec)) (range steps))))
  ([end steps] (frange 0.0 end steps)))

(defn rand-between
  "Returns a random float between low & high."
  [low high]
  (+ low (rand (- high low))))

(defn rand-vec
  "Returns a randomly generated vector of three floats.
   Called with no args each component is just (rand),
   with one arg, they are given by (rand to), and
   with two args by (rand-between low high)."
  ([] [(rand) (rand) (rand)])
  ([to] [(rand to) (rand to) (rand to)])
  ([low high]
     [(rand-between low high)
      (rand-between low high)
      (rand-between low high)]))

(defn rand-elem
  "Takes a collection and returns a random element in
   the collection. Calls vec & count on the collection,
   so you should take that into account."
  [col]
  ((vec col) (rand-int (count col))))

(defmacro rand-expr
  "Takes an arbitrary number of expressions and
   evaluates one at random. Only in LISP!"
  [& exprs]
  (let [n (count exprs)
        probs (map #(/ % n) (range 1 n))
        clauses (interleave probs exprs)]
    `(condp > (rand) ~@clauses ~(last exprs))))