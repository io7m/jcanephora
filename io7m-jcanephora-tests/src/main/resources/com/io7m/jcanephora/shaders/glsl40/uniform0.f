#version 40

uniform float alpha;
out vec4 out_color;

void
main (void)
{
  out_color = vec4(1.0, 0.0, 0.0, alpha);
}
