#version 110

attribute float a_f;
attribute vec2 a_vf2;
attribute vec3 a_vf3;
attribute vec4 a_vf4;
attribute float a_f_opt;
attribute vec2 a_vf2_opt;
attribute vec3 a_vf3_opt;
attribute vec4 a_vf4_opt;
uniform float u_float;
uniform int u_int;
uniform vec2 u_vf2;
uniform vec3 u_vf3;
uniform vec4 u_vf4;
uniform ivec2 u_vi2;
uniform ivec3 u_vi3;
uniform ivec4 u_vi4;
uniform mat3 u_m3;
uniform mat4 u_m4;
uniform float u_float_opt;
uniform int u_int_opt;
uniform vec2 u_vf2_opt;
uniform vec3 u_vf3_opt;
uniform vec4 u_vf4_opt;
uniform ivec2 u_vi2_opt;
uniform ivec3 u_vi3_opt;
uniform ivec4 u_vi4_opt;
uniform mat3 u_m3_opt;
uniform mat4 u_m4_opt;
varying vec4 f_position_clip;
varying vec4 something;

void
main (void)
{
  vec4 pl_vf0 = vec4 (a_vf2.x, a_vf3.x, a_vf4.z, u_float);
  vec4 pl_vf1 = vec4 (u_vf2.x, u_vf3.x, u_vf4.x, float (u_int));
  vec4 pl_vf2 = vec4 (a_f, a_f, a_f, a_f);
  vec4 pl_vf3 = vec4 ((u_m3 * a_vf3), 1.0);
  vec4 pl_vf4 = (pl_vf0 + pl_vf1);
  vec4 pl_vf5 = (pl_vf2 + pl_vf3);
  vec4 pl_vf6 = (pl_vf4 + pl_vf5);
  ivec4 pl_vi0 = ivec4 (u_vi2, u_vi3.x, u_vi3.y);
  ivec4 pl_vi1 = ivec4 (u_vi4.xyz, u_int);
  ivec4 pl_vi2 = (pl_vi0 + pl_vi1);
  vec4 pl_k = vec4 ((float (pl_vi2.x) + pl_vf6.x), (float (pl_vi2.y) + pl_vf6.y), (float (pl_vi2.z) + pl_vf6.z), (float (pl_vi2.w) + pl_vf6.w));
  vec4 pl_clip_position = (u_m4 * pl_k);
  gl_Position = pl_clip_position;
  f_position_clip = pl_clip_position;
  something = pl_k;
}
