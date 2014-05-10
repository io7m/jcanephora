#version 410

in vec4 something;
uniform sampler2D u_sampler2d;
uniform samplerCube u_sampler_cube;
out vec4 out_0;

void
main (void)
{
  vec4 pl_t2 = texture (u_sampler2d, something.xy);
  vec4 pl_tc = texture (u_sampler_cube, something.xyy);
  vec4 pl_rgba = (pl_t2 + pl_tc);
  out_0 = pl_rgba;
}
