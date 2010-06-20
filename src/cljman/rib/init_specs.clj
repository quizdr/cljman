
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns cljman.rib.init-specs
  (:use [cljman.rib.emit :only (emit)])
  (:require [clojure.contrib.string :as s])
  (:use [clojure.contrib.def :only (defn-memo)])
  (:require [cljman.rib.specs :as specs]))

(let [doc-plea
      "Please write a doc string for this command once
       you have figured it out and submit it to the project.
       Thank You!"]
  (defn- rib-doc-for-spec [spec]
    (str "RIB command: " (:rib-cmnd spec) "\n"
         (or (:doc spec) doc-plea))))

(defn- rib-fn-def [{:keys [name rib-cmnd args has-param-list] :as spec}]
  (let [doc (rib-doc-for-spec spec)
        arg-list (map symbol args)
        arg-vec (vec (concat arg-list (if has-param-list '(& param-list))))
        param-list (if has-param-list 'param-list)]
    `(defn ~(symbol name) ~doc ~arg-vec
       (apply emit ~rib-cmnd ~@arg-list ~param-list))))

(defn- rib-cmnd-for-name [name]
  (apply str (map s/capitalize (s/split #"-" name))))

(defn-memo specs []
  (map (fn [spec]
         (assoc spec :rib-cmnd
                (spec :rib-cmnd (rib-cmnd-for-name (spec :name)))))
       specs/rib-cmnd-specs))

(defn init-spec-functions []
  (let [fn-defs (map rib-fn-def (specs))]
    (eval (cons 'do fn-defs))))
