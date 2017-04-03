#version 330 core

uniform float f;
uniform vec2 fv2;
uniform vec3 fv3;
uniform vec4 fv4;

uniform int i;
uniform ivec2 iv2;
uniform ivec3 iv3;
uniform ivec4 iv4;

uniform uint u;
uniform uvec2 uv2;
uniform uvec3 uv3;
uniform uvec4 uv4;

uniform bool b;
uniform bvec2 bv2;
uniform bvec3 bv3;
uniform bvec4 bv4;

void
main (void)
{
  vec4 q = vec4 (f);
  q = q + vec4 (fv2, 1.0, 1.0);
  q = q + vec4 (fv3, 1.0);
  q = q + vec4 (fv4);

  q = q + vec4 (float (i));
  q = q + vec4 (vec2 (iv2), 1.0, 1.0);
  q = q + vec4 (vec3 (iv3), 1.0);
  q = q + vec4 (iv4);

  q = q + vec4 (float (u));
  q = q + vec4 (vec2 (uv2), 1.0, 1.0);
  q = q + vec4 (vec3 (uv3), 1.0);
  q = q + vec4 (uv4);

  q = q + vec4 (float (b));
  q = q + vec4 (vec2 (bv2), 1.0, 1.0);
  q = q + vec4 (vec3 (bv3), 1.0);
  q = q + vec4 (bv4);

  gl_Position = q;
}
