#version 100

uniform float u_float;
uniform mat3  u_mat3;
uniform mat4  u_mat4;
uniform vec2  u_vec2;
uniform ivec2 u_ivec2;
uniform vec3  u_vec3;
uniform ivec3 u_ivec3;
uniform vec4  u_vec4;
uniform ivec4 u_ivec4;

void
main (void)
{
  float x = u_float;
  x = x + u_mat3[0][0];
  x = x + u_mat4[0][0];
  x = x + u_vec2.x;
  x = x + float(u_ivec2.x);
  x = x + u_vec3.x;
  x = x + float(u_ivec3.x);
  x = x + u_vec4.x;
  x = x + float(u_ivec4.x);

  gl_Position = vec4(x, x, x, 1.0);
}