
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns cljman.rib
  (:use [cljman.rib.emit :only (emit emit-closed)])
  (:use [cljman.rib.init-specs :only (init-spec-functions)])
  (:require [cljman.colors :as colors]))

(init-spec-functions)

;; block commands

(defmacro frame
  "RIB Commands: FrameBegin & FrameEnd
   Usually used to represent frames in an animation. Contains one
   or more world blocks and their options. Pushes both the attribute
   and options state. May not be self-nested. The frame-number is
   descriptive only. "
  [frame-number & exprs]
  `(emit-closed "Frame" [~frame-number] ~exprs))

(defmacro world
  "RIB Commands: WorldBegin & WorldEnd
   Contains the scene description of a single image. The current
   transformation is stored as the camera to world matrix, the
   object to world matrix is initialized to the identity matrix,
   and the entire attribute stack is pushed. Several calls to world
   can be nested within a call to frame. May not be self-nested."
  [& exprs]
  `(emit-closed "World" [] ~exprs))

(defmacro attribute
  "RIB Commands: AttributeBegin & AttributeEnd
   (Note that RIB's Attribute is called set-attribute in cljman)
   Pushes the entire attributes state including the transformation
   stack before executing exprs, after which the stacks are popped.
   May be used inside or outside a call to world. May be nested
   within calls to transform and vice-versa."
  [& exprs]
  `(emit-closed "Attribute" [] ~exprs))

(defmacro transform
  "RIB Commands: TransformBegin & TransformEnd
   (Note that RIB's Transform is called set-transform in cljman)
   Pushes only the transformation stack, executes exprs, and pops
   the stack. May be nested within calls to attribute and vice-versa."
  [& exprs]
  `(emit-closed "Transform" [] ~exprs))

(defmacro solid [operation & exprs]
  `(emit-closed "Solid" [~operation] ~exprs))

(defmacro motion [times-vector & exprs]
  `(emit-closed "Motion" [~times-vector] ~exprs))

;; lights and objects
(let [rib-id (atom 0)]
  (defn gen-rib-id []
    (swap! rib-id
           #(if (>= % 65535) 1 (inc %)))))

(defmacro object [& exprs]
  (let [object-id (gen-rib-id)
        object-fn #(emit "ObjectInstance" object-id)]
    `(emit-closed "Object" [~object-id] ~exprs ~object-fn)))

(defmacro defobject [name & exprs]
  `(def ~name (object ~@exprs)))

(defn- light [rib-cmnd shader-name param-list]
  (let [light-id (gen-rib-id)]
    (apply emit rib-cmnd shader-name light-id param-list)
    (fn illuminate
      ([onoff] (emit "Illuminate" light-id onoff))
      ([] (illuminate true)))))

(defn light-source
  ""
  [shader-name & param-list]
  (light "LightSource" shader-name param-list))

(defmacro def-light-source
  ""
  [name shader-name & param-list]
  `(def ~name (light-source ~shader-name ~@param-list)))

(defn area-light-source
  ""
  [shader-name & param-list]
  (light "AreaLightSource" shader-name param-list))

(defmacro def-area-light-source
  ""
  [name shader-name & param-list]
  `(def ~name (area-light-source ~shader-name ~@param-list)))

;; misc commands

(defn color
  "Sets the default color of the following primitives.
   color - A vector, list or seq of n floats (by default 3)
           in the range [0, 1]. Note that n can be changed
           via color-samples. color can also be used by directly
           passing floats, as in renderman: (color 0 1 0).
           If a single keyword is passed then the color is
           as defined in cljman.colors/color-map.
           You can add your own colors to the map with add-color."
  [& color]
  (let [frst (first color)
        col (cond (keyword? frst) (colors/get-color frst)
                  (vector? frst) frst
                  :else color)]
    (emit "Color" col)))

(defn opacity
  "Sets the default opacity of the following primitives.
   color - A vector, list, or seq, of n floats (by default 3) in the
           range [0, 1]. Note that n can be changed via color-samples.
           If a single numerical value is passed, then a vector containing
           3 repetitions of the number is created for you. opacity can also be
           used by directly passing floats, as in renderman: (opacity 0 1 0).
           If you are working with a single channel image, just pass in a vector
           containing a single float to avoid the auto-expansion.

           Examples: (opacity [1 0 0])   -> Opacity [1 0 0]
                     (opacity 0.4 0 1/2) -> Opacity [0.4 0 0.5]
                     (opacity 3/4)       -> Opacity [0.75 0.75 0.75]
                     (opacity [3/4])     -> Opacity [0.75]"
  [& color]
  (let [frst (first color)
        col (if (number? frst)
              (if (= (count color) 1)
                (take 3 (repeat frst))
                color)
              frst)]
    (emit "Opacity" col)))
