#version 100

precision highp float;
precision highp int;

uniform sampler2D sampler;

void
main (void)
{
  gl_FragColor = texture2D(sampler, vec2(23, 34));
}
