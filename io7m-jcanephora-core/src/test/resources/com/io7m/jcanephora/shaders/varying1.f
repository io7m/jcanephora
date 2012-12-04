#version 110

varying vec3 vary0;

void
main (void)
{
  gl_FragColor = vec4(vary0.xy, 0.0, 1.0);
}
