#version 130

uniform sampler2D sampler;
out vec4 out_color;

void
main (void)
{
  out_color = texture2D(sampler, vec2(23, 34));
}
