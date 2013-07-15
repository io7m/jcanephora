#version 40

struct color_t {
  float red;
  float green;
  float blue;
};

uniform color_t color;

out vec4 out_color;

void
main ()
{
  out_color = vec4(color.red, color.green, color.blue, 1.0);
}
