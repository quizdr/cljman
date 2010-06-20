
// This shader written by Peter Wertz who was
// gracious enough to share it with us. Thanks!
// http://www.petewertz.com/electron_index.html

surface electron_mic(
  float Kd = 1; 
  color coloredge = color (0.529,0.529,0.427)
) {
  float rim_width = .01;

  normal n = normalize(N);
  normal nf = faceforward(n, I, n);;

  vector i = normalize(-I);
  float  dot = 1 - i.nf;

  Oi = smoothstep(1 - rim_width, 1.0, dot);
  Oi = 1;

  color diffusecolor = 1 - nf.i;

  Ci = Oi * Cs * diffusecolor * coloredge * Kd;
}
