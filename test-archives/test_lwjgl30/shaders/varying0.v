#version 110

varying float vary0;

void
main (void)
{
  gl_Position = ftransform();
  vary0 = ftransform().x;
}
