#version 330 core

layout(location = 0) in vec4 p;

void
main (void)
{
  gl_Position = p;
}
