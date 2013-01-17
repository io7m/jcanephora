#version 130

uniform float alpha;

void
main (void)
{
  gl_FragColor = vec4(1.0, 0.0, 0.0, alpha);
}
