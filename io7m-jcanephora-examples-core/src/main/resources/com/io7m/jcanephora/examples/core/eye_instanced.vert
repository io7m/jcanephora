#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in mat4 m_model;

uniform mat4 m_view;
uniform mat4 m_projection;

void
main ()
{
  gl_Position = (m_projection * m_view * m_model) * vec4 (position, 1.0);
}
