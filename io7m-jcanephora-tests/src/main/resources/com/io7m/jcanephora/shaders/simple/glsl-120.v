#version 120

attribute vec3 v_position;
uniform mat4 m_modelview;
uniform mat4 m_projection;
varying vec4 f_position_clip;

void
main (void)
{
  vec4 clip_position = ((m_projection * m_modelview) * vec4 (v_position, 1.0));
  gl_Position = clip_position;
  f_position_clip = clip_position;
}
