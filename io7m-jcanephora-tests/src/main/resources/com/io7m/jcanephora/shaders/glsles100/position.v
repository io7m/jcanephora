#version 100

precision highp float;
precision highp int;

attribute vec3 position;

void
main (void)
{
  gl_Position = vec4(position, 1.0);
}
