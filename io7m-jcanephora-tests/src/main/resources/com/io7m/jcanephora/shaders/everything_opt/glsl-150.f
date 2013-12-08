#version 150


uniform sampler2D u_texture;
uniform sampler2D u_texture_opt;
in vec4 something;
out vec4 out_0;


void
main (void)
{
  vec4 rgba = texture(u_texture, something.xy);
  out_0 = rgba;
}
