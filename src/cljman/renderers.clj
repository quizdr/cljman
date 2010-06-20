
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns cljman.renderers
  (:require [cljman.renderers.pixie :as pixie]
            [cljman.renderers.aqsis :as aqsis]
            [cljman.renderers.delight :as delight])
  (:use cljman.process))

(let [base-map
      {:pixie pixie/info
       :aqsis aqsis/info
       :delight delight/info}]
  (defn info
    "get particulars for the various third party binaries.
     :cmnd-names, :renderer-options, and :shader-compiler-options
     are currently available. example:

     (cljman.renderers/info :aqsis :cmnd-names :renderer) -> \"aqsis\""
    [& keys]
    (get-in base-map keys)))

(defn render
  "vendor should be a symbol, :aqsis, :pixie or :delight, the three
   currently supported renderers.

   render-fn is a function which should output rib commands to *out*;
   calling any of cljman's rib commands will do the trick.

   Note that if your render function does any other writing to
   *out* it will end up in the rib stream and could corrupt it
   (see rib-comment to output legal comments).

   render accepts a variable number of option/value pairs after
   the function, which are renderer specific. you can see the options
   at the repl by:

   (cljman.renderers/info :pixie :renderer-options).

   Note that if your render-fn expects args then you should
   pass these in a sequence, by supplying :render-args [...],
   as an option."
  [vendor render-fn & options]
  (let [renderer-opts (info vendor :renderer-options)
        cmnd-name (info vendor :cmnd-names :renderer)
        user-opts (apply hash-map options)]
    (pipe-out-to-process
     (shell-command-string renderer-opts cmnd-name user-opts)
     render-fn
     (user-opts :render-args))))

(defn compile-shaders
  "vendor should be a symbol, :aqsis, :pixie or :delight, the three
   currently supported renderers.

   paths should be a sequence of filepaths or directory paths.
   If a directory path is supplied then all the shaders in the directory
   are compiled.

   In the following, foo.sl, and bar.sl as well as well as all shaders in
   foobar/ will be compiled.

   (compile-shaders [\"foo.sl\" \"bar.sl\" \"foobar/\"])

   A variable number of option/value pairs which are renderer specific can
   be passed as additional arguments. You can see these at the repl by:

   (cljman.renderers/info :aqsis :shader-compiler-options)"
  [vendor path & options]
  )