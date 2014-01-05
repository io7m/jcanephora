#version 100

precision highp float;
precision highp int;

varying vec4 something;
uniform sampler2D u_texture;

void
main (void)
{
  vec4 rgba = texture2D (u_texture, something.xy);
  gl_FragColor = rgba;
}
