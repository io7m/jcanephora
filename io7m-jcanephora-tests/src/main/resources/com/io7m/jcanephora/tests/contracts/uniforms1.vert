#version 330 core

uniform mat4 fm4;
uniform mat4x4 fm4x4;
uniform mat4x3 fm4x3;
uniform mat4x2 fm4x2;

uniform mat3 fm3;
uniform mat3x4 fm3x4;
uniform mat3x3 fm3x3;
uniform mat3x2 fm3x2;

uniform mat2 fm2;
uniform mat2x4 fm2x4;
uniform mat2x3 fm2x3;
uniform mat2x2 fm2x2;

void
main (void)
{
  vec4 q = vec4 (1.0);
  q = q + vec4 (fm4   [0][0], 1.0, 1.0, 1.0);
  q = q + vec4 (fm4x4 [0][0], 1.0, 1.0, 1.0);
  q = q + vec4 (fm4x3 [0][0], 1.0, 1.0, 1.0);
  q = q + vec4 (fm4x2 [0][0], 1.0, 1.0, 1.0);

  q = q + vec4 (fm3   [0][0], 1.0, 1.0, 1.0);
  q = q + vec4 (fm3x4 [0][0], 1.0, 1.0, 1.0);
  q = q + vec4 (fm3x3 [0][0], 1.0, 1.0, 1.0);
  q = q + vec4 (fm3x2 [0][0], 1.0, 1.0, 1.0);

  q = q + vec4 (fm2   [0][0], 1.0, 1.0, 1.0);
  q = q + vec4 (fm2x4 [0][0], 1.0, 1.0, 1.0);
  q = q + vec4 (fm2x3 [0][0], 1.0, 1.0, 1.0);
  q = q + vec4 (fm2x2 [0][0], 1.0, 1.0, 1.0);

  gl_Position = q;
}
