#version 100

precision highp float;
precision highp int;

varying float vary0;

void
main (void)
{
  gl_Position = vec4(1,2,3,1);
  vary0 = 100.0;
}
