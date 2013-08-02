#version 130

out vec4 out0;
out vec4 out1;
out vec4 out2;
out vec4 out3;

void
main (void)
{
  out0 = vec4(1, 0, 0, 1);
  out1 = vec4(0, 1, 0, 1);
  out2 = vec4(0, 0, 1, 1);
  out3 = vec4(0, 1, 1, 0);
}
