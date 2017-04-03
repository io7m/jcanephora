#version 330 core

layout(location = 0) out vec4 R2_out;

uniform vec2 data[8];

void
main (void)
{
  vec2 sum = vec2 (0.0);
  for (int index = 0; index < 8; ++index) {
    sum += data [index];
  }

  R2_out = vec4 (sum, 0.0, 1.0);
}
