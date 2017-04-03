#version 330 core

layout(location = 0) out vec4 R2_out;

uniform vec4 data[8];

void
main (void)
{
  vec4 sum = vec4 (0.0);
  for (int index = 0; index < 8; ++index) {
    sum += data [index];
  }

  R2_out = sum;
}
