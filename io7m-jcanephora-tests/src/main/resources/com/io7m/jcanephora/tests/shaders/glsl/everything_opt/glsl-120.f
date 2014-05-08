#version 120

varying vec4 something;
uniform sampler2D u_sampler2d;
uniform sampler2D u_sampler2d_opt;

void
main (void)
{
  vec4 pl_rgba = texture2D (u_sampler2d, something.xy);
  gl_FragColor = pl_rgba;
}
