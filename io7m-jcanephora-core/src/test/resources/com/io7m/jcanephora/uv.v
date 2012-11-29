#version 110

uniform mat4 matrix_projection;
uniform mat4 matrix_modelview;

attribute vec4 vertex_position;
attribute vec2 vertex_uv;

varying vec2 fragment_uv;

void main (void)
{
  gl_Position = matrix_projection * matrix_modelview * vertex_position;
  fragment_uv = vertex_uv;
}