# cljman

[Renderman](http://www.faqs.org/faqs/graphics/renderman-faq/)
"bindings" for clojure. Really a set of emiter
functions which generate RIB as output
(renderman interface bytestream). 

The [RenderMan Interface](https://renderman.pixar.com/products/rispec/index.htm) 
is a standard interface between modeling programs and rendering programs
capable of producing photorealistic quality computer graphics images.
It's kind of like the postscript of 3D. cljman could serve as the
basis on which to develop modelling programs or to just do algoritmic 
output destined for a renderer.

A few renderers, currently [pixie](http://www.renderpixie.com), 
[aqsis](http://www.aqsis.org/), and
[3delight](http://www.3delight.com/en/) 
are directly supported by cljman, but you should be
able to pump out RIB to any compliant renderer including pixar's.
Pixar if you are reading this, I'd be glad to add support if you 
grant me a licence ;)

Also included is a 3D mathematics library and some convenience
utilities for common tasks. 

In the development branch can be found (incomplete), a RIB parser, and an experimental shape synthesiser based on Andrew Glassner's 
[shape synth](http://www.glassner.com/andrew/cg/research/shapesynth/shapesynth.htm), 
with some ideas from James McCartney's [Supercollider](http://www.audiosynth.com/)

The base rib functions and utilities will probably not change
much but the math libs, shape synth, and renderer support may change 
considerably with future releases. Future releases may also include tools for 
generating shaders from clojure, as well as opengl preview (would
appreciate some coloborators, especially on the opengl preview
as time constraints will probably not allow me to develop it). 

Criticism and especially code contributions are most welcome.

## Usage

To use the rib generation facilities as well as a few additional
rib helper functions, use or require cljman.rib and cljman.rib.util.
I might make the latter included automatically by the former shortly.

Also functional at this point are the cljman.math & cljman.renderers
files. They are adequately documented. 

All examples should now be working as well.

## Installation

Download it [here](http://github.com/polypus74/cljman/downloads), or get it with git:

    $ git clone git://github.com/polypus74/cljman.git

You can also get it off [clojars](http://clojars.org/cljman).

## License

Copyright (c) 2010 Christophe McKeon

cljman is released under the Eclipse Public License. 
See epl-v10.html distributed with this software.

No Waranty & Disclaimer of Liability, see clauses 5 & 6 of epl-v10.html
