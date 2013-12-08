#version 420


uniform mat4 m_modelview;
uniform mat4 m_projection;
in vec3 v_position;


void
main (void)
{
  vec4 clip_position = ((m_projection * m_modelview) * vec4(v_position, 1.0));
  gl_Position = clip_position;
}
