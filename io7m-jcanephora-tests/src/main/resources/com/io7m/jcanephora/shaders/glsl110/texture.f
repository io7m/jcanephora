#version 110

uniform sampler2D sampler;

void
main (void)
{
  gl_FragColor = texture2D(sampler, vec2(23, 34));
}
