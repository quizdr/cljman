
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns cljman.rib.parse
  (:use [cljman.rib :only (gen-rib-id)])
  (:use [cljman.rib.init-specs :only (specs)])
  (:require [clojure.contrib.string :as s])
  (:require [clojure.walk :as walk]))

;; a not particularly robust rib parser
;; which returns clojure expressions
;;
;; rib grammar, roughly
;; cmnd-list -> cmnd cmnd-list |
;;              <empty>
;; cmnd -> simple-cmnd |
;;         block-cmnd
;; simple-cmnd -> symbol param-list
;; param-list -> param param-list |
;;               <empty>
;; block-cmnd -> start-symbol param-list cmnd-list end-symbol

;; TODO become aware of hashes in strings
(defn strip-comments [rib]
  (let [rib-lines (s/split #"\n" rib)]
    (s/join "\n" (for [line rib-lines]
                   (let [[_ s] (re-find #"^(.*?)#.*$" line)]
                     (or s line))))))

(defn- lex [rib]
  ;; rib is lisp!
  (read-string (str "(" rib ")")))

(defn- parse-error [expected but-got]
  (let [err-str (str "Parse Error, expected: "
                     expected " but got: " but-got)]
    (throw (Exception. err-str))))

(def begin-block-cmnd?
     '#{FrameBegin WorldBegin AttributeBegin
        TransformBegin SolidBegin MotionBegin ObjectBegin})

(def end-block-cmnd?
     '#{FrameEnd WorldEnd AttributeEnd
        TransformEnd SolidEnd MotionEnd ObjectEnd})

(def begin-matches-end?
     (memoize
      (fn [begin-sym end-sym]
        true)))

(def clj-cmnd-for-name
  (let [cmnds-not-in-spec
        '{FrameBegin frame
          WorldBegin world
          AttributeBegin attribute
          TransformBegin transform
          SolidBegin solid
          MotionBegin motion
          Color color
          ;; these are post processed after the
          ;; parse. see cljmanize-parsed
          ObjectBegin :object
          ObjectInstance :object-instance
          LightSource :light-source
          AreaLightSource :area-light-source
          Illuminate :illuminate
          Opacity :opacity}
        spec-cmnds
        (apply hash-map
               (mapcat
                (fn [{:keys (rib-cmnd name)}]
                  [(symbol rib-cmnd) (symbol name)])
                (specs)))]
    (merge cmnds-not-in-spec spec-cmnds)))

(defn- convert-name [name-sym]
  (if-let [clj-name (clj-cmnd-for-name name-sym)]
    clj-name
    (parse-error "a valid rib command" name-sym)))

(defn- simple-cmnd [name toks]
  (let [[params more] (split-with #(not (symbol? %)) toks)]
    [(apply list (convert-name name) params) more]))

(declare cmnd-list)

(defn- block-cmnd [name toks]
  (let [[params more] (split-with #(not (symbol? %)) toks)
        clj-name (convert-name name)
        cmnd-base (apply list clj-name params)
        [sub-cmnds more] (cmnd-list more)
        cmnd-with-subs (concat cmnd-base sub-cmnds)
        look (first more)]
    (if (begin-matches-end? name look)
      [cmnd-with-subs (rest more)]
      (parse-error (str "matching end statement for " name)
                   look))))

;; TODO remove an if
(defn- cmnd [[look & more]]
  (if (symbol? look)
    (if (begin-block-cmnd? look)
     (block-cmnd look more)
     (simple-cmnd look more))))

(defn- cmnd-list [toks]
  (if (and (seq toks) (not (end-block-cmnd? (first toks))))
    (let [[a-cmnd more] (cmnd toks)
          [more-cmnds more] (cmnd-list more)]
      [(cons a-cmnd more-cmnds) more])
    [nil toks]))

(declare *parse-state*)

(def cljmanize-functions
     {:object
      (fn []
        (concat '(defobject foobar) (drop 2 obj-cmnd)))
      :object-instance
      (fn [obj] 'TODO)
      :light-source
      (fn [obj]
        (let [shader-name (second obj)
              params (drop 3 obj)
              light-name (symbol (str "_auto_light_" (gen-rib-id)))]
          (apply list 'def-light-source light-name params)))
      :area-light-source
      (fn [obj] 'TODO)
      :illuminate
      (fn [obj] 'TODO)
      :opacity
      (fn [obj] 'TODO)})

(defn cljmanize-parsed [obj]
  (or (if (seq? obj)
        (let [frst (first obj)]
          (if (keyword? frst)
            ((cljmanize-functions frst) obj))))
      obj))

(defn parse [rib]
  (let [pure-rib (strip-comments rib)
        tokens (lex pure-rib)]
    (cons 'do (first (cmnd-list tokens)))))

(def rib (slurp "/Users/polypus/Documents/creations/software/cljman/src/cljman/rib/bezier.rib"))

(def rib_ "Display \"foo.tif\" \"framebuffer\" \"rgb\"
          Translate 0 0 6 Opacity [0 1 2] WorldBegin Sphere 1 -1 1 360 WorldEnd")

(use 'cljman.render 'cljman.rib 'clojure.contrib.pprint)
;(aqsis-render #(eval (parse rib)))
                                        ; (pr(parse rib))
(def parsed (parse rib))

(binding [*parse-state* {}]
  (pprint (walk/prewalk cljmanize-parsed parsed)))