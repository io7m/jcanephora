#version 100

precision highp float;
precision highp int;

uniform sampler2D u_texture;
varying vec4 something;



void
main (void)
{
  vec4 rgba = texture2D(u_texture, something.xy);
  gl_FragColor = rgba;
}
