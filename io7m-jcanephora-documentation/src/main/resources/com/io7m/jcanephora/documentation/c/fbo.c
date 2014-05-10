#define GL_GLEXT_PROTOTYPES 1

#include <assert.h>
#include <err.h>
#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>
#include <GL/glut.h>
#include <GL/glext.h>

static unsigned int depth_buffer;
static unsigned int frame_buffer;
static unsigned int texture;

static void
init_data()
{
  glGenTextures(1, &texture);
  assert(glGetError() == GL_NO_ERROR);
  glBindTexture(GL_TEXTURE_2D, texture);
  assert(glGetError() == GL_NO_ERROR);
  glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
  assert(glGetError() == GL_NO_ERROR);
  glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
  assert(glGetError() == GL_NO_ERROR);
  glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
  assert(glGetError() == GL_NO_ERROR);
  glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
  assert(glGetError() == GL_NO_ERROR);
  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 256, 256, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
  assert(glGetError() == GL_NO_ERROR);
  glBindTexture(GL_TEXTURE_2D, 0);
  assert(glGetError() == GL_NO_ERROR);

  glGenFramebuffers(1, &frame_buffer);
  assert(glGetError() == GL_NO_ERROR);
  glBindFramebuffer(GL_FRAMEBUFFER, frame_buffer); 
  assert(glGetError() == GL_NO_ERROR);

  glGenRenderbuffers(1, &depth_buffer);
  assert(glGetError() == GL_NO_ERROR);
  glBindRenderbuffer(GL_RENDERBUFFER, depth_buffer);
  assert(glGetError() == GL_NO_ERROR);
  glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, 256, 256);
  assert(glGetError() == GL_NO_ERROR);
  glBindRenderbuffer(GL_RENDERBUFFER, 0);
  assert(glGetError() == GL_NO_ERROR);

  glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);
  assert(glGetError() == GL_NO_ERROR);
  glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depth_buffer);
  assert(glGetError() == GL_NO_ERROR);

  {
    GLenum status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
    assert(glGetError() == GL_NO_ERROR);
    assert(status == GL_FRAMEBUFFER_COMPLETE);
  }

  glBindFramebuffer(GL_FRAMEBUFFER, 0);
  assert(glGetError() == GL_NO_ERROR);
}

static void
display(void)
{
  /*
   * Render to texture.
   */

  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  glFrustum(-1, 1, -1, 1, 1, 100);
  assert(glGetError() == GL_NO_ERROR);

  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();
  glTranslated(0, 0, -2);
  assert(glGetError() == GL_NO_ERROR);

  glBindFramebuffer(GL_FRAMEBUFFER, frame_buffer);
  assert(glGetError() == GL_NO_ERROR);

  glClearColor(0.0, 0.0, 1.0, 1.0);
  glClear(GL_COLOR_BUFFER_BIT);

  glBegin(GL_TRIANGLES);
  {
    glColor3d(1.0, 0.0, 0.0);
    glVertex3d(0.0, 1.0, 0.0);
    glVertex3d(0.0, 0.0, 0.0);
    glVertex3d(1.0, 0.0, 0.0);
  }
  glEnd();

  glBindFramebuffer(GL_FRAMEBUFFER, 0);
  assert(glGetError() == GL_NO_ERROR);

  /*
   * Draw quad with contents of framebuffer.
   */

  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  glFrustum(-1, 1, -1, 1, 1, 100);
  assert(glGetError() == GL_NO_ERROR);

  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();
  glTranslated(0, 0, -2);
  assert(glGetError() == GL_NO_ERROR);

  glClearColor(0.2, 0.2, 0.2, 1.0);
  glClear(GL_COLOR_BUFFER_BIT);
  assert(glGetError() == GL_NO_ERROR);

  glEnable(GL_TEXTURE_2D);
  assert(glGetError() == GL_NO_ERROR);
  glBindTexture(GL_TEXTURE_2D, texture);
  assert(glGetError() == GL_NO_ERROR);

  glBegin(GL_QUADS);
  {
    glColor3d(1.0, 1.0, 1.0);
    glTexCoord2d(0.0, 1.0);
    glVertex3d(0.0, 1.0, 0.0);
    glTexCoord2d(0.0, 0.0);
    glVertex3d(0.0, 0.0, 0.0);
    glTexCoord2d(1.0, 0.0);
    glVertex3d(1.0, 0.0, 0.0);
    glTexCoord2d(1.0, 1.0);
    glVertex3d(1.0, 1.0, 0.0);
  }
  glEnd();

  glDisable(GL_TEXTURE_2D);
  assert(glGetError() == GL_NO_ERROR);
  glBindTexture(GL_TEXTURE_2D, 0);
  assert(glGetError() == GL_NO_ERROR);

  glutSwapBuffers();
}

int
main (int argc, char **argv)
{
  glutInit(&argc, argv);
  glutInitDisplayMode(GLUT_DOUBLE);
  glutInitWindowSize(256, 256);
  glutCreateWindow("FBO");

  init_data();
  glutDisplayFunc(display);
  glutIdleFunc(glutPostRedisplay);
  glutMainLoop();
  return 0;
}

