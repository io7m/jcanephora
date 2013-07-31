#version 40

uniform sampler2D u_t;
uniform float u_f;

out vec4 out_frag;

void
main (void)
{
  out_frag = texture2D(u_t, vec2(u_f, u_f));
}
