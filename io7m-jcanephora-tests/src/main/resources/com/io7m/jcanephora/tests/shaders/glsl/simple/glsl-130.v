#version 130

in vec3 v_position;
uniform mat4 m_modelview;
uniform mat4 m_projection;
out vec4 f_position_clip;

void
main (void)
{
  vec4 pl_clip_position = ((m_projection * m_modelview) * vec4 (v_position, 1.0));
  gl_Position = pl_clip_position;
  f_position_clip = pl_clip_position;
}
