#version 100

precision highp float;
precision highp int;

attribute vec4 vertex;

void
main (void)
{
  gl_Position = vertex;
}
