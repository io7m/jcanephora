#version 330 core

uniform sampler2D s2;
uniform sampler3D s3;
uniform samplerCube sc;

void
main (void)
{
  vec4 q = vec4 (1.0);

  q = q + texture(s2, vec2(0.0));
  q = q + texture(s3, vec3(0.0));
  q = q + texture(sc, vec3(0.0));

  gl_Position = q;
}
