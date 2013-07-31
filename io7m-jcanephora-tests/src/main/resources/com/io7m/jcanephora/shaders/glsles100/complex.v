#version 100

attribute vec2 a_vf2;
attribute vec3 a_vf3;
attribute vec4 a_vf4;

attribute float a_f;

uniform vec2 u_vf2;
uniform vec3 u_vf3;
uniform vec4 u_vf4;

uniform ivec2 u_vi2;
uniform ivec3 u_vi3;
uniform ivec4 u_vi4;

uniform mat3 u_m3;
uniform mat4 u_m4;

void
main (void)
{
  vec4 vf0 = vec4 (a_vf2, 1, 1) + vec4 (a_vf3, 1) + a_vf4;
  vec4 vf1 = vec4 (a_f, a_f, a_f, a_f);
  vec4 vf2 = vec4 (u_vf2, 1, 1) + vec4 (u_vf3, 1) + u_vf4;
  vec4 vf3 = vec4 (vec2(u_vi2), 1, 1) + vec4 (vec3(u_vi3), 1) + vec4 (u_vi4);
  vec4 vf4 = u_m4 * vf3;
  vec4 vf5 = vec4 (u_m3 * a_vf3, 1);

  gl_Position = vf0 + vf1 + vf2 + vf3 + vf4 + vf5;
}
