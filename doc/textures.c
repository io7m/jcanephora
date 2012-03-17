#define GL_GLEXT_PROTOTYPES 1

#include <assert.h>
#include <err.h>
#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>
#include <GL/glut.h>
#include <GL/glext.h>

typedef struct {
  unsigned char r;
  unsigned char g;
  unsigned char b;
  unsigned char a;
} abgr_t;

static GLuint pixel_buffer;
static GLuint texture;
static const GLuint texture_size = 4 * 4 * sizeof(abgr_t);

static void
init_data()
{
  glGenBuffers(1, &pixel_buffer);
  glBindBuffer(GL_PIXEL_UNPACK_BUFFER, pixel_buffer);
  glBufferData(GL_PIXEL_UNPACK_BUFFER, texture_size, NULL, GL_STREAM_DRAW);
  assert(glGetError() == GL_NO_ERROR);

  {
    int x;
    int y;
    abgr_t *map = glMapBuffer(GL_PIXEL_UNPACK_BUFFER, GL_WRITE_ONLY);
    assert (map != NULL);

    for (y = 0; y < 4; ++y) {
      double fy = y;
      for (x = 0; x < 4; ++x) {
        double fx = x;
        map->r = (unsigned char) (256.0 * (fx / 4.0));
        map->g = (unsigned char) (256.0 * (fy / 4.0));
        map->b = 0xff;
        map->a = 0xff;
        ++map;
      }
    }

    glUnmapBuffer(GL_PIXEL_UNPACK_BUFFER);
    assert(glGetError() == GL_NO_ERROR);
  }

  glGenTextures(1, &texture);
  glBindTexture(GL_TEXTURE_2D, texture);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 4, 4, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);
  assert(glGetError() == GL_NO_ERROR);

  glBindTexture(GL_TEXTURE_2D, 0);
  glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0);
}

static void
reshape(int width, int height)
{
  assert (width >= 0);
  assert (height >= 0);
}

static void
display(void)
{

}

int
main (int argc, char **argv)
{
  glutInit(&argc, argv);
  glutCreateWindow("Textures");
  init_data();
  glutReshapeFunc(reshape);
  glutDisplayFunc(display);
  glutIdleFunc(glutPostRedisplay);

  glutMainLoop();
  return 0;
}

