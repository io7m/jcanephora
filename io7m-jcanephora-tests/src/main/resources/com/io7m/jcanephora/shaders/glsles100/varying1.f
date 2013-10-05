#version 100

precision highp float;
precision highp int;

varying vec3 vary0;

void
main (void)
{
  gl_FragColor = vec4(vary0.xy, 0.0, 1.0);
}
