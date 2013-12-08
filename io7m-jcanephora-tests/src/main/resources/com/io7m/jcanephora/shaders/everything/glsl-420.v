#version 420


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
in float a_f;
in vec2 a_vf2;
in vec3 a_vf3;
in vec4 a_vf4;
out vec4 something;


void
main (void)
{
  vec4 vf0 = vec4(a_vf2.x, a_vf3.x, a_vf4.z, u_float);
  vec4 vf1 = vec4(u_vf2.x, u_vf3.x, u_vf4.x, float(u_int));
  vec4 vf2 = vec4(a_f, a_f, a_f, a_f);
  vec4 vf3 = vec4((u_m3 * a_vf3), 1.0);
  vec4 vf4 = (vf0 + vf1);
  vec4 vf5 = (vf2 + vf3);
  vec4 vf6 = (vf4 + vf5);
  ivec4 vi0 = ivec4(u_vi2, u_vi3.x, u_vi3.y);
  ivec4 vi1 = ivec4(u_vi4.xyz, u_int);
  ivec4 vi2 = (vi0 + vi1);
  vec4 k = vec4((float(vi2.x) + vf6.x), (float(vi2.y) + vf6.y), (float(vi2.z) + vf6.z), (float(vi2.w) + vf6.w));
  vec4 clip_position = (u_m4 * k);
  gl_Position = clip_position;
  something = k;
}
