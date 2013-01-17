#version 130

attribute vec4 vertex;

void
main (void)
{
  gl_Position = vertex;
}
