
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns cljman.rib.util
  (:use [cljman rib util colors]
        [clojure.contrib.string :only (split-lines ltrim)]))

;; convenience

(defn bilinear-patch
  "A covenience function which:
     (apply patch :bilinear :P args)"
  [& args]
  (apply patch :bilinear :P args))

(defn bicubic-patch
  "A covenience function which:
     (apply patch :bicubic :P args)"
  [& args]
  (apply patch :bicubic :P args))

(defn point-light
  "A convenience function for:
     (apply light-source :pointlight :from from :intensity intensity args)"
  [from intensity & args]
  (apply light-source :pointlight :from from :intensity intensity args))

(defn distant-light
  "A convenience function for:
     (apply light-source :distantlight :from from :to to :intensity intensity args)"
  [from to intensity & args]
  (apply light-source :distantlight :from from :to to :intensity intensity args))

(defn ambient-light
  "A convenience function for:
    (apply light-source :ambientlight :intensity intensity args)"
  [intensity & args]
  (apply light-source :ambientlight :intensity intensity args))

(defn spot-light
  "A convenience function for:
    (apply light-source :spotlight :from from :to to :intensity intensity args)"
  [from to intensity & args]
  (apply light-source :spotlight :from from :to to :intensity intensity args))

(defn displacement-bound
  "A convenience function for:
    (set-attribute :displacementbound shape magnitude :coordinatesystem space)
   space defaults to :world in the single arity version, and shape
   defaults to :sphere in the single and double arity versions."
  ([magnitude]
     (displacement-bound magnitude :world :sphere))
  ([magnitude space]
     (displacement-bound magnitude space :sphere))
  ([magnitude space shape]
     (set-attribute :displacementbound shape magnitude :coordinatesystem space)))

(defn perspective-projection
  "A convenience function for:
    (apply projection :perspective :fov [fov] args)"
  [fov & args]
  (apply projection :perspective :fov [fov] args))

(defn rand-color
  "Defines a random color. If called with no args
   generates a completely random color. If called with
   args, either vectors, or symbols as defined in cljman.colors,
   then one of those is picked randomly."
  ([]
     (color (rand-vec)))
  ([& colors]
     (color (rand-elem colors))))

(defn rib-comment
  "Emits a legal RIB comment. For each line of text, trimming any
   leading whitespace and then preceding it with a # symbol."
  [text]
  (doseq [line (split-lines text)]
    (println "#" (ltrim line))))

;; preview

(def preview-defaults {:low {:display ["rough-preview.tiff" :framebuffer :rgb]
                               :image-format [300 300 1.0]
                               :pixel-samples [1 1]
                               :pixel-filter [:box 1 1]
                               :shading-rate 16}
                       :medium {:display ["medium-preview.tiff" :framebuffer :rgb]
                                :image-format [550 550 1.0]
                                :pixel-samples [2 2]
                                :pixel-filter [:gaussian 2 2]
                                :shading-rate 4}
                       :high {:display ["fine-preview.tiff" :framebuffer :rgb]
                              :image-format [800 800 1.0]
                              :pixel-samples [4 4]
                              :pixel-filter [:sinc 4 4]
                              :shading-rate 1}})

(defn preview-fn
  "A convenience function which takes a render function (your rib generating function),
   and returns another function of variable arity which wraps your render function with
   calls to set up display, image-format, pixel-samples, pixel-filter & shading-rate.
   Any args passed to the returned function are passed to your wrapped function when
   it is called.

   The second parameter of quality determines which set of default options is used.
   You can view the defaults for the qualities of :rough, :medium, and :high by
   going (preview-defaults quality-keyword).

   In addition key value pairs can be passed to override certain default options if
   desired.
   (preview-fn my-rib-fn :rough :image-format [100 100 1.4] :shading-rate 8)"
  [render-fn quality & user-opts]
  (let [opts (merge (preview-defaults quality) (apply hash-map user-opts))]
    (fn [& args]
      (apply display (opts :display))
      (apply image-format (opts :image-format))
      (apply pixel-samples (opts :pixel-samples))
      (apply pixel-filter (opts :pixel-filter))
      (shading-rate (opts :shading-rate))
      (apply render-fn args))))


