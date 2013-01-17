#version 130

uniform float float_0;
uniform mat3 mat3_0;
uniform mat4 mat4_0;
uniform vec2 vec2_0;
uniform ivec2 vec2_1;
uniform vec3 vec3_0;
uniform vec4 vec4_0;

void
main (void)
{
  gl_Position = vec4(float_0 + mat3_0[0][0] + mat4_0[0][0],
                     vec2_0.x + float (vec2_1.x),
                     vec3_0.x + float (vec4_0.x),
                     1.0);
}
