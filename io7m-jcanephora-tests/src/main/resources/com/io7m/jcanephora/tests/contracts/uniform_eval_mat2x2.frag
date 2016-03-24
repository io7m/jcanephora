#version 330 core

layout(location = 0) out vec4 R2_out;

uniform mat2x2 data[8];

void
main (void)
{
  float sum = 0.0;
  for (int index = 0; index < 8; ++index) {
    sum += data [index][0][0];
    sum += data [index][0][1];
    sum += data [index][1][0];
    sum += data [index][1][1];
  }

  R2_out = vec4 (sum, 0.0, 0.0, 1.0);
}
