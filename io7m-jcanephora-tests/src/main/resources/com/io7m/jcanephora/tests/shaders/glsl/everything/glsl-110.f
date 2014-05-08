#version 110

varying vec4 something;
uniform sampler2D u_sampler2d;
uniform samplerCube u_sampler_cube;

void
main (void)
{
  vec4 pl_t2 = texture2D (u_sampler2d, something.xy);
  vec4 pl_tc = textureCube (u_sampler_cube, something.xyy);
  vec4 pl_rgba = (pl_t2 + pl_tc);
  gl_FragColor = pl_rgba;
}
