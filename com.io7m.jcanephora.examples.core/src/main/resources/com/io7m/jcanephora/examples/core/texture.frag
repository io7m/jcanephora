#version 330 core

in vec2 f_uv;

uniform sampler2D t_albedo;

layout(location = 0) out vec4 out_albedo;

void
main ()
{
  out_albedo = texture(t_albedo, f_uv);
}
