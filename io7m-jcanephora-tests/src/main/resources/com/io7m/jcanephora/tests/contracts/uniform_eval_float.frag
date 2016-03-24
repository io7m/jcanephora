#version 330 core

layout(location = 0) out vec4 R2_out;

uniform float data[8];

void
main (void)
{
  float sum = 0.0;
  for (int index = 0; index < 8; ++index) {
    sum += data [index];
  }

  R2_out = vec4 (sum, sum, sum, 1.0);
}
