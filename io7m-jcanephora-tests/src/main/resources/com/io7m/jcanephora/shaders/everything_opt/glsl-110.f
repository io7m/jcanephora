#version 110

varying vec4 something;
uniform sampler2D u_texture;
uniform sampler2D u_texture_opt;

void
main (void)
{
  vec4 rgba = texture2D (u_texture, something.xy);
  gl_FragColor = rgba;
}
