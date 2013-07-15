#version 110

struct color_t {
  float red;
  float green;
  float blue;
};

uniform color_t color;

void
main ()
{
  gl_FragColor = vec4(color.red, color.green, color.blue, 1.0);
}
