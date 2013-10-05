#version 100

precision highp float;
precision highp int;

uniform sampler2D u_t;
uniform float u_f;

void
main (void)
{
  gl_FragColor = texture2D(u_t, vec2(u_f, u_f));
}
