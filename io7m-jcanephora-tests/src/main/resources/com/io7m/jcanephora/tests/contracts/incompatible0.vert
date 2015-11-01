#version 330 core

layout(location = 0) in vec4 p;

out int out_0;

void
main (void)
{
  gl_Position = p;
}
