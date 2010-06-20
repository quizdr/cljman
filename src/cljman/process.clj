
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

;; This file used internally by cljman, you probably
;; don't need to include it directly

(ns cljman.process
  (:import (java.io InputStreamReader OutputStreamWriter)))

(defn- read-stream [stream]
  (let [reader (InputStreamReader. stream)]
    (apply str (map char (take-while #(>= % 0)
                                     (repeatedly #(.read reader)))))))

(defn pipe-out-to-process
  [proc-name out-fn out-fn-args]
  (let [proc (.exec (Runtime/getRuntime) proc-name)]
    (with-open [osw (OutputStreamWriter. (.getOutputStream proc))]
      (binding [*out* osw] (apply out-fn out-fn-args)))
    (with-open [stdout (.getInputStream proc)
                stderr (.getErrorStream proc)]
      (let [out (read-stream stdout)
            err (read-stream stderr)
            exit-code (.waitFor proc)]
        {:exit exit-code :out out :err err}))))

(defn colon-separated [& args]
  (apply str (interpose ":" args)))

(defn spaced-str [& vals]
  (apply str (interpose " " vals)))

(defn shell-command-string [options-map cmnd-name options]
  (let [opt-strings (filter string?
                            (map (fn [[opt-name val]]
                                   (let [transform-or-string (options-map opt-name)]
                                     (if (string? transform-or-string)
                                       (if val transform-or-string)
                                       (transform-or-string val))))
                                 options))]
    (apply spaced-str cmnd-name opt-strings)))

