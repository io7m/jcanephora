#define GL_GLEXT_PROTOTYPES 1

#include <assert.h>
#include <err.h>
#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>

#include <GL/glut.h>
#include <GL/glext.h>
#include <tiffio.h>

static GLuint pixel_buffer;
static GLuint texture;

static void
init_data()
{
  uint32 width;
  uint32 height;
  uint32 texture_size;

  TIFF *tiff = TIFFOpen("32.tiff", "r");
  assert (tiff != NULL);

  TIFFGetField(tiff, TIFFTAG_IMAGEWIDTH, &width);
  TIFFGetField(tiff, TIFFTAG_IMAGELENGTH, &height);
  texture_size = width * height * 4;

  glGenBuffers(1, &pixel_buffer);
  glBindBuffer(GL_PIXEL_UNPACK_BUFFER, pixel_buffer);
  glBufferData(GL_PIXEL_UNPACK_BUFFER, texture_size, NULL, GL_STREAM_DRAW);
  assert(glGetError() == GL_NO_ERROR);

  {
    unsigned char *map = glMapBuffer(GL_PIXEL_UNPACK_BUFFER, GL_WRITE_ONLY);
    int r = TIFFReadRGBAImage(tiff, width, height, (uint32 *) map, 0);
    assert (r != 0);
    glUnmapBuffer(GL_PIXEL_UNPACK_BUFFER);
    TIFFClose(tiff);
  }

  glGenTextures(1, &texture);
  glBindTexture(GL_TEXTURE_2D, texture);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL);
  assert(glGetError() == GL_NO_ERROR);

  glBindTexture(GL_TEXTURE_2D, 0);
  glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0);
}

static void
reshape(int width, int height)
{
  assert (width > 0);
  assert (height > 0);
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
  glutReshapeFunc(reshape);
  glutIdleFunc(glutPostRedisplay);

  glutMainLoop();
  return 0;
}

