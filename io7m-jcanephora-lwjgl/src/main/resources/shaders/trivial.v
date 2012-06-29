#version 110

attribute vec3 position;
attribute vec3 color;

uniform mat4 matrix_projection;
uniform mat4 matrix_modelview;

varying vec4 out_color;

void
main (void)
{
  vec4 p      = matrix_projection * matrix_modelview * vec4(position, 1.0);
  gl_Position = p;
  out_color   = vec4(color.rgb, 1.0);
}
