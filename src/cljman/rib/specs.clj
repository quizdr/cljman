
;; cljman, renderman for clojure

;; Copyright (c) Christophe McKeon, 2010. All rights reserved. The use
;; and distribution terms for this software are covered by the Eclipse
;; Public License 1.0 (http://www.eclipse.org/legal/epl-v10.html
;; which can be found in the file epl-v10.html at the root of this
;; distribution. By using this software in any fashion, you are
;; agreeing to be bound by the terms of this license. You must not
;; remove this notice, or any other, from this software.

(ns cljman.rib.specs)

;; NOTE: do not access these directly, use
;;  cljman.rib.init-specs/specs instead
(def rib-cmnd-specs
     [{:name "version"
       :args ["number"]
       :rib-cmnd "version"}
      {:name "declare-var"
       :args ["name" "declaration"]
       :rib-cmnd "Declare"}
      {:name "image-format"
       :args ["xresolution" 
              "yresolution"
              "pixel-aspect-ratio"]
       :rib-cmnd "Format"
       :doc "Specifies the resolution of the image.
             pixel-aspect-ratio - determines the shape of the pixels
                    in the image. usually this is set to 1.0.
             Note that these settings can conflict with options
             set via frame-aspect-ratio for example. How the
             renderer deals with inconsistencies in implementation
             specific."}
      {:name "frame-aspect-ratio"
       :args ["frameaspectratio"]}
      {:name "screen-window"
       :args ["left"
              "right"
              "bottom"
              "top"]}
      {:name "crop-window"
       :args ["xmin"
              "xmax"
              "ymin"
              "ymax"]
       :doc "Specifies in \"NDC\" coordinates a sub-portion of the
             image to be rendered. All args are floats and are hence
             resolution independent. The upper left quarter of an
             image can thus be rendered like so: (crop-window 0 0.5 0 0.5)
             Rounding of floats is always dependable for image tiling
             purposes."}
      {:name "projection"
       :args ["name"]
       :has-param-list true}
      {:name "clipping"
       :args ["near" "far"]
       :doc "Sets the near and far clipping planes. You should
             strive to set this as the defaults of near zero
             and infinity are not friendly to your renderer's
             floating point precision in it's perspective calculations."}
      {:name "clipping-plane"
       :args ["x"  "y"  "z"
              "nx"  "ny"  "nz"]}
      {:name "depth-of-field"
       :args ["fstop"
              "focallength"
              "focaldistance"]}
      {:name "shutter"
       :args ["min"  "max"]}
      {:name "pixel-variance"
       :args ["variation"]}
      {:name "pixel-samples"
       :args ["xsamples" "ysamples"]
       :doc "Controls the level of antialiasing by specifying
             the supersampling rate per pixel per axis.
             For simple antialiasing 2 by 2 is a good setting.
             For more stochastic sampling 8 by 8 or higher might
             be tried.
             xsamples - the number of samples per pixel on the x axis
             ysamples - ditto for the y axis"}
      {:name "pixel-filter"
       :args ["type"
              "xwidth"
              "ywidth"]
       :doc "Specifies the type and width of the pixel reconstruction
             filter.
             type - name of the filter type like \"box\" or \"sinc\"
             xwidth & ywidth - a xwidth of 3.0 would cover 1 pixel to
                               the left & right of the pixel in question."}
      {:name "exposure"
       :args ["gain"  "gamma"]
       :doc "Sets the gamma correction of the image. By default
             there is no automatic gamma correction in renderman.
             Useful for lightening or darkening up an image without
             messing with the light sources.
             gain - a float which scales all output values uniformly
             gamma - a float which specifies the gamma correction"}
      {:name "imager"
       :args ["name"]
       :has-param-list true}
      {:name "quantize"
       :args ["type"
              "one"
              "min"
              "max"
              "dither-amplitude"]}
      {:name "display"
       :args ["name"
              "type"
              "mode"]
       :doc "Sets the name and format of the output image. Usually
             called before world.
             name - specifies the file name if a file is being
                    outputted but can also specify a window
                    title for example if type is :framebuffer.
             type - specifies an image format like :tiff and :cineon
                    or the more generic :file or :framebuffer
             mode - specifies the image data mode like :rgb or :rgba"
       :has-param-list true}
      {:name "hider"
       :args ["type"]
       :has-param-list true
       :doc "Specifies the hidden surface removal algorithm.
             type - the algorithm to use defaults to \"hidden\"
                    other algorithms are \"null\" & \"paint\" the
                    latter of which always paints the last primitive on
                    top but may not be supported by your renderer."}
      {:name "color-samples"
       :args ["nRGB"
              "RGBn"]}
      {:name "relative-detail"
       :args ["relativedetail"]}
      {:name "option"
       :args ["name"]
       :has-param-list true
       :doc "Sets renderer specific options which are global to
             the scene. Options which are not supported by your
             renderer are ignored."}
      {:name "texture-coordinates"
       :args ["s1"  "t1"
              "s2"  "t2"
              "s3"  "t3"
              "s4"  "t4"]}
      {:name "surface"
       :args ["shadername"]
       :has-param-list true
       :doc "Specifies the surface shader (color/opacity) to use
             on the following primitives. Parameters to the shader
             are supplied via param-list. Surface shaders run after
             displacement and before atmospheric (volume) shaders."}
      {:name "displacement"
       :args ["shadername"]
       :has-param-list true
       :doc "Specifies the displacement mapping shader
             (surface perturbation) to use on the following primitives.
             Parameters to the shader are supplied via param-list.
             Displacement shaders run before surface shaders."}
      {:name "atmosphere"
       :args ["shadername"]
       :has-param-list true
       :doc "Specifies the volume shader (atmospheric color contribution)
             to use on the following primitives. Parameters to the shader
             are supplied via param-list. Atmospheric shaders run
             after surface shaders."}
      {:name "interior"
       :args ["shadername"]
       :has-param-list true}
      {:name "exterior"
       :args ["shadername"]
       :has-param-list true}
      {:name "shading-rate"
       :args ["size"]
       :doc "Specifies the maximum distance between shading samples.
             A shading rate of 1.0 might be used for a high quality
             rendering whereas a rate between 4 to 8 might be used
             for a lower qualiy one.
             size - a float which gives the area in pixels of the
                    largest region on the geometric primitive that
                    any given shading sample can represent"}
      {:name "shading-interpolation"
       :args ["type"]
       :doc "Determines the type of shading interpolation between
             shading samples.
             type - can be :constant or :smooth (bilinear/Gouraud)"}
      {:name "matte"
       :args ["onoff"]
       :doc "Sets the 3D holdout matte flag to true or false.
             Matte primitives and portions of primitives behind them
             are not rendered in the final frame but objects in front
             are rendered normally.
             onoff - you can pass an integer as you would in a rib file
                     but cljman will correctly convert a boolean as well"}
      {:name "bound"
       :args ["bound"]}
      {:name "detail"
       :args ["bound"]}
      {:name "detail-range"
       :args ["minvisible"
              "lowertransition"
              "uppertransition"
              "maxvisible"]}
      {:name "geometric-approximation"
       :args ["type"
              "value"]}
      {:name "orientation"
       :args ["orientation"]}
      {:name "reverse-orientation"}
      {:name "sides"
       :args ["sides"]}
      {:name "reset-identity"
       :rib-cmnd "Identity"
       :doc "Replaces the current transformation matrix with the
             identity matrix. Inside a world call has the effect of
             reseting the local coordinate system to the world system
             and outside a world call to the camera coordinate system."}
      {:name "set-transform"
       :args ["matrix"]
       :rib-cmnd "Transform"
       :doc "Replaces the current transformation matrix with the
             supplied matrix. Equivalent to:
             (do (reset-identity)
                 (concat-transform matrix))"}
      {:name "concat-transform"
       :args ["matrix"]
       :doc "Premultiplies given matrix into the current transformation
             matrix.
             matrix - is a vector of 16 floats which transforms
                      points from the new coordinate system to the
                      previous one."}
      {:name "perspective"
       :args ["fov"]}
      {:name "translate"
       :args ["dx"
              "dy"
              "dz"]}
      {:name "rotate"
       :args ["angle"
              "dx"
              "dy"
              "dz"]
       :doc "Rotates the local coordinate system by angle degrees
             around the axis given by the direction vector
             (dx dy dz)."}
      {:name "scale"
       :args ["sx"
              "sy"
              "sz"]
       :doc "Scales the local coordinate system by
             sx in x sy in y and sz in z."}
      {:name "skew"
       :args ["angle"
              "dx1" "dy1" "dz1"
              "dx2" "dy2" "dz2"]}
      {:name "coordinate-system"
       :args ["name"]
       :doc "Labels the local coordinate system with a name so
             that it can be refered to later by other commands
             and shaders. i.e. allows for user defined
             coordinate systems."}
      {:name "coord-sys-transform"
       :args ["name"]
       :doc "Replaces the current transformation matrix with the
             matrix refered to by name. See also: coordinate-system."}
      {:name "set-attribute"
       :args ["name"]
       :has-param-list true
       :rib-cmnd "Attribute"}
      {:name "polygon"
       :has-param-list true}
      {:name "general-polygon"
       :args ["nvertices"]
       :has-param-list true}
      {:name "points-polygons"
       :args ["nvertices"
              "vertices"]
       :has-param-list true}
      {:name "points-general-polygons"
       :args ["nloops"
              "nvertices"
              "vertices"]
       :has-param-list true}
      {:name "basis"
       :args ["ubasis"
              "ustep"
              "vbasis"
              "vstep"]}
      {:name "patch"
       :args ["type"]
       :has-param-list true}
      {:name "patch-mesh"
       :args ["type"
              "nu"
              "uwrap"
              "nv"
              "vwrap"]
       :has-param-list true}
      {:name "nu-patch"
       :args ["nu"
              "uorder" "uknot"
              "umin"
              "umax"
              "nv"
              "vorder"
              "vknot"
              "vmin"
              "vmax"]
       :has-param-list true}
      {:name "trim-curve"
       :args ["nloops"
              "ncurves"
              "order"
              "knot"
              "min"
              "max"
              "n"
              "u"
              "v"
              "w"]}
      {:name "subdivision-mesh"
       :args ["scheme"
              "nfaces"
              "nvertices"
              "vertices"
              "ntags"
              "nargs"
              "intargs"
              "floatargs"]
       :has-param-list true}
      {:name "sphere"
       :args ["radius"
              "zmin"
              "zmax"
              "thetamax"]
       :has-param-list true}
      {:name "cone"
       :args ["height"
              "radius"
              "thetamax"]
       :has-param-list true}
      {:name "cylinder"
       :args ["radius"
              "zmin"
              "zmax"
              "thetamax"]
       :has-param-list true}
      {:name "hyperboloid"
       :args ["point1"
              "point2"
              "thetamax"]
       :has-param-list true}
      {:name "paraboloid"
       :args ["rmax"
              "zmin"
              "zmax"
              "thetamax"]
       :has-param-list true}
      {:name "disk"
       :args ["height"
              "radius"
              "thetamax"]
       :has-param-list true}
      {:name "torus"
       :args ["majorradius"
              "minorradius"
              "phimin"
              "phimax"
              "thetamax"]
       :has-param-list true}
      {:name "points"
       :has-param-list true}
      {:name "curves"
       :args ["type"
              "nvertices"
              "wrap"]
       :has-param-list true}
      {:name "blobby"
       :args ["nleaf"
              "code"
              "floats"
              "strings"]
       :has-param-list true}
      {:name "procedural"
       :args ["procname"
              "args"
              "bound"]}
      {:name "geometry"
       :args ["name"]
       :has-param-list true}
      {:name "make-texture"
       :args ["picturename"
              "texturename"
              "swrap"
              "twrap"
              "filterfunc"
              "swidth"
              "twidth"]
       :has-param-list true}
      {:name "make-lat-long-environment"
       :args ["picturename"
              "texturename"
              "filter"
              "swidth"
              "twidth"]
       :has-param-list true}
      {:name "make-cube-face-environment"
       :args ["px"
              "nx"
              "py"
              "ny"
              "pz"
              "nz"
              "texturename"
              "fov"
              "filter"
              "swidth"
              "twidth"]
       :has-param-list true}
      {:name "make-shadow"
       :args ["picturename"
              "texturename"]
       :has-param-list true}
      {:name "set-error-handle"
       :args ["handler"]
       :rib-cmnd "ErrorHandler"}
      {:name "read-archive"
       :args ["filename"]
       :has-param-list true}])



