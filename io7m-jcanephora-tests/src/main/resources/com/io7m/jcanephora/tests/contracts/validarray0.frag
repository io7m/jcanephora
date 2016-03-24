#version 330 core

layout(location = 0) out vec4 p;

uniform vec4 big_array[16];

void
main (void)
{
  vec4 sum = vec4 (0.0);
  for (int index = 0; index < 16; ++index) {
    sum += big_array[index];
  }

  p = sum;
}
