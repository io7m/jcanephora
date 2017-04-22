#version 330 core

layout(location = 0) out vec4 R2_out;

uniform mat4x4 data[8];

void
main (void)
{
  float sum = 0.0;
  for (int index = 0; index < 8; ++index) {
    sum += data [index][0][0];
    sum += data [index][0][1];
    sum += data [index][0][2];
    sum += data [index][0][3];

    sum += data [index][1][0];
    sum += data [index][1][1];
    sum += data [index][1][2];
    sum += data [index][1][3];

    sum += data [index][2][0];
    sum += data [index][2][1];
    sum += data [index][2][2];
    sum += data [index][2][3];

    sum += data [index][3][0];
    sum += data [index][3][1];
    sum += data [index][3][2];
    sum += data [index][3][3];
  }

  R2_out = vec4 (sum, 0.0, 0.0, 1.0);
}
