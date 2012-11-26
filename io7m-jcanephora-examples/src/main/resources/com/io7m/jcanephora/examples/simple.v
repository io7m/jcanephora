uniform mat4 matrix_projection;
uniform mat4 matrix_modelview;

attribute vec4 vertex_color;
attribute vec4 vertex_position;

varying fragment_color;

void main()
{
  gl_Position = matrix_projection * matrix_modelview * vertex_position;
  fragment_color = vertex_color;
}