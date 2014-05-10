#define GL_GLEXT_PROTOTYPES 1

#include <assert.h>
#include <err.h>
#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>

#include <GL/glut.h>
#include <GL/glext.h>

static GLuint texture;

static void
init_data()
{
  uint32_t x;
  uint32_t y;
  uint32_t width = 64;
  uint32_t height = 64;
  unsigned char *data;

  data = malloc((width * 4) * height);
  if (data == NULL) {
    err(1, "malloc");
  }

  for (y = 0; y < height; ++y) {
    for (x = 0; x < height; ++x) {
      const double red   = x / 64.0;
      const double green = random();
      const double blue  = y / 64.0;

      uint32_t offset = (y * (width * 4)) + (x * 4);
      data[offset + 0] = (unsigned char) (red * 0xff);
      data[offset + 1] = (unsigned char) (green * 0xff);
      data[offset + 2] = (unsigned char) (blue * 0xff);
      data[offset + 3] = (unsigned char) 0xff;
    }
  }

  glGenTextures(1, &texture);
  glBindTexture(GL_TEXTURE_2D, texture);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
  assert(glGetError() == GL_NO_ERROR);

  glBindTexture(GL_TEXTURE_2D, 0);
  free(data);
}

static void
display(void)
{
  glClearColor(0.2, 0.2, 0.2, 1.0);
  glClear(GL_COLOR_BUFFER_BIT);

  glEnable(GL_TEXTURE_2D);
  glActiveTexture(GL_TEXTURE0);
  glBindTexture(GL_TEXTURE_2D, texture);

  glBegin(GL_QUADS);
  {
    glTexCoord2d(0.0, 1.0);
    glVertex3d(-0.5, 0.5, 0.0);
    glTexCoord2d(0.0, 0.0);
    glVertex3d(-0.5, -0.5,  0.0);
    glTexCoord2d(1.0, 0.0);
    glVertex3d(0.5, -0.5, 0.0);
    glTexCoord2d(1.0, 1.0);
    glVertex3d(0.5, 0.5, 0.0);
  }
  glEnd();

  glutSwapBuffers();
}

int
main (int argc, char **argv)
{
  glutInit(&argc, argv);
  glutInitDisplayMode(GLUT_DOUBLE);  
  glutInitWindowSize(256, 256);
  glutCreateWindow("Textures");

  init_data();
  glutDisplayFunc(display);
  glutIdleFunc(glutPostRedisplay);
  glutMainLoop();
  return 0;
}

