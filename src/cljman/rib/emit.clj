
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns cljman.rib.emit)

(def emit-raw println)

(defn- dblquote [s]
  (str "\"" s "\""))

(defmulti rib-format type)

(defmethod rib-format String [s]
  (dblquote s))

(defmethod rib-format clojure.lang.Symbol [s]
  (dblquote s))

(defmethod rib-format clojure.lang.Keyword [k]
  (dblquote (name k)))

(defmethod rib-format clojure.lang.Ratio [r]
  (float r))

(defmethod rib-format Number [n] n)

(defmethod rib-format Boolean [b]
  (if b 1 0))

;; anything that gets this far has to be a collection
(defmethod rib-format :default [obj]
  (vec (map rib-format obj)))

(defn emit [rib-name & args]
  (apply emit-raw rib-name (map rib-format args)))

(defmacro emit-closed
  ([rib-name args exprs]
     `(emit-closed ~rib-name ~args ~exprs nil))
  ([rib-name args exprs return]
     `(do
        (emit (str ~rib-name "Begin") ~@args)
        ~@exprs
        (emit-raw (str ~rib-name "End"))
        ~return)))
