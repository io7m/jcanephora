#version 300

precision highp float;
precision highp int;

in vec4 something;
uniform sampler2D u_texture;
out vec4 out_0;

void
main (void)
{
  vec4 rgba = texture (u_texture, something.xy);
  out_0 = rgba;
}
