#version 330

in vec4 something;
uniform sampler2D u_sampler2d;
uniform sampler2D u_sampler2d_opt;
out vec4 out_0;

void
main (void)
{
  vec4 pl_rgba = texture (u_sampler2d, something.xy);
  out_0 = pl_rgba;
}
