#version 110

uniform sampler2D texture;

varying vec2 fragment_uv;

void main (void)
{
  gl_FragColor = texture2D(texture, fragment_uv);
}