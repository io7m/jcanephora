#version 330 core

layout(location = 0) in mat4x4 m4;
layout(location = 4) in mat4x3 m3;
layout(location = 8) in mat4x2 m2;

void
main (void)
{
  vec4 q = vec4 (m4[0]);
  q = q + vec4 (m3 [0], 1.0);
  q = q + vec4 (m2 [0], 1.0, 1.0);

  gl_Position = q;
}
