#version 330 core

layout(location = 0) out vec4 R2_out;

uniform vec3 data[8];

void
main (void)
{
  vec3 sum = vec3 (0.0);
  for (int index = 0; index < 8; ++index) {
    sum += data [index];
  }

  R2_out = vec4 (sum, 1.0);
}
