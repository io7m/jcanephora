#version 130

varying vec3 vary0;
out vec4 out_color;

void
main (void)
{
  out_color = vec4(vary0.xy, 0.0, 1.0);
}
