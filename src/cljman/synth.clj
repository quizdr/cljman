
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns cljman.synth
  (:require [cljman.rib :as rib])
  (:use [cljman.math :only [magnitude cross rad2deg squared]])
  (:use [cljman.util :only [frange]]))

;; A good pour of  Andrew Glassner's Shape Synthesizer, as
;; presented in his Other Notebook, pg. 224, as well
;; as a splash of James McCartney's Supercollider multichannel
;; expansion semantics, shaken & with a lazy clojuresque twist...

;; triple -> [1 2 3] | '(1 2 3)
;; channel -> '(triple*)
;; multi-channel '(channel+)

(defn triple? [in]
  (and (sequential? in)
       (number? (first in))))

(defn channel? [in]
  (and (sequential? in)
       (if (empty? in)
         true
         (triple? (first in)))))

(defn multi-channel? [in]
  (and (sequential? in)
       (not (empty? in))
       (every? channel? in)))

;; in-specs# is a list of maps, each containing a channels field
;; which contains a list of sequences of triples.
;; inf-channels# is a list of lists of sequences of triples,
;; where the lists of sequences are infinite cycles.

(defmacro defvgen [fname doc expanded-params params & body]
  (let [all-params (vec (concat expanded-params params))]
    `(defn ~fname ~doc ~all-params
       (let [in-specs# (for [in# ~expanded-params]
                         (cond (triple? in#)
                               {:channels (list (repeat in#))
                                :num-channels 1}
                               (channel? in#)
                               {:channels (list in#)
                                :num-channels 1}
                               (multi-channel? in#)
                               {:channels in#
                                :num-channels (count in#)}
                               :uhoh (throw (IllegalArgumentException.
                                             (str "bad input to " ~fname)))))
             max-channels# (apply max (map :num-channels in-specs#))
             action# (fn ~(gensym fname) ~all-params ~@body)]
         (if (= max-channels# 1)
           (apply action# (concat (map #(first (:channels %)) in-specs#) ~params))
           (let [inf-channels# (map #(cycle (:channels %)) in-specs#)]
             (doall ;; for the polygon nodes to be properly consumed
              (take max-channels#
                    (apply map action#
                           (concat inf-channels#
                                   (map repeat ~params)))))))))))

;; (defmacro def-basic-vgen [fname doc params & body]
;;   `(defvgen ~fname ~doc ~params []
;;      (apply map (fn ~params ~@body) ~params)))

;; (def-basic-vgen vct*
;;   "vector controlled translator"
;;   [in by]
;;   (map + in by))

(defvgen vct
  "vector controlled translator"
  [in by] []
  (map #(map + %1 %2) in by))

(defvgen vcs
  "vector controlled scaling"
  [in by] []
  (map #(map * %1 %2) in by))

(comment
  (defvgen vcr
    "vector controlled rotation"
    [in base axis] []
    (map
     (fn [in base axis]
       (let [[x y z] in
             [a b c] base
             [u v w] axis
             theta (magnitude axis)
             u2 (squared u) v2 (squared v) w2 (squared w)
             v2+w2 (+ v2 w2) u2+w2 (+ u2 w2) u2+v2 (+ u2 v2)
             u2+v2+w2 (+ u2+v2 w2)
             sqrt-u2+v2+w2 (Math/sqrt u2+v2+w2)
             cos-theta (Math/cos theta)
             sin-theta (Math/sin theta)
             [au av aw
              bu bv bw
              cu cv cw]
             (for [b base a axis] (* b a))
             [ux uy uz
              vx vy vz
              wx wy wz]
             (for [a axis i in] (* a i))
             ux+vy+wz (+ ux vy wz)
             -cw+ux+vy+wz (- (+ cw ux+vy+wz))
             xcomp (+ (* a v2+w2)
                      (* u (- -cw+ux+vy+wz bv))
                      (+ (* (- x a) v2+w2)
                         (* u ())))
             ycomp
             zcomp]
         (map #(/ % u2+v2+w2) [xcomp ycomp zcomp])))
     in base axis)))

(defvgen vch
  ""
  [] []
  )

(defvgen vcc
  ""
  [in0 in1] []
  (map cross in0 in1))

(defvgen dly
  "delay line"
  [in] [n]
  (concat (repeat n [0 0 0]) in))

(defvgen tri
  ""
  [v0 v1 v2] []
  (dorun (map #(rib/polygon "P" (concat %1 %2 %3))
              v0 v1 v2)))

(defvgen pnt
  ""
  [pos] [width]
  (rib/points "P" (apply concat pos) :constantwidth width))

(defn sweep
  ""
  ([start end steps]
     (apply map vector
            (map frange start end (repeat steps))))
  ([start end xsteps ysteps zsteps]
     (for [])))







