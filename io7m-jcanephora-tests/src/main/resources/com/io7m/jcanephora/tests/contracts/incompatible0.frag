#version 330 core

in float out_0;

layout(location = 0) out vec4 p;

void
main (void)
{
  p = vec4 (out_0);
}
