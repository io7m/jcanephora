#version 330 core

layout(location = 0) in float f;
layout(location = 1) in vec2 fv2;
layout(location = 2) in vec3 fv3;
layout(location = 3) in vec4 fv4;

layout(location = 4) in int i;
layout(location = 5) in ivec2 iv2;
layout(location = 6) in ivec3 iv3;
layout(location = 7) in ivec4 iv4;

layout(location = 8) in uint u;
layout(location = 9) in uvec2 uv2;
layout(location = 10) in uvec3 uv3;
layout(location = 11) in uvec4 uv4;

void
main (void)
{
  vec4 q = vec4 (f);
  q = q + vec4 (fv2, fv2);
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

  gl_Position = q;
}
