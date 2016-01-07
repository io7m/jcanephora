#version 330 core

layout(location = 0) in vec3 position;

uniform mat4 m_modelview;
uniform mat4 m_projection;

void
main ()
{
  gl_Position = (m_projection * m_modelview) * vec4 (position, 1.0);
}
