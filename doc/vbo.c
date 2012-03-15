#define GL_GLEXT_PROTOTYPES 1

#include <assert.h>
#include <err.h>
#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>
#include <GL/glut.h>
#include <GL/glext.h>

typedef float vector3[3];

typedef struct
{
  vector3 position;
  vector3 color;
} vertex;

static GLuint buffer;
static GLuint triangles[2];

static void
init_data()
{
  unsigned int elements = 4;
  unsigned int size = elements * sizeof(vertex);

  glGenBuffers(1, &buffer);
  glBindBuffer(GL_ARRAY_BUFFER, buffer);
  glBufferData(GL_ARRAY_BUFFER, size, NULL, GL_STREAM_DRAW);
  assert (glGetError() == GL_NO_ERROR);

  {
    vertex *map = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY);
    assert (map != NULL);

    map[0].position[0] = 0.0f;
    map[0].position[1] = 1.0f;
    map[0].position[2] = 0.0f;
    map[0].color[0] = 1.0f;
    map[0].color[1] = 0.0f;
    map[0].color[2] = 0.0f;

    map[1].position[0] = 0.0f;
    map[1].position[1] = 0.0f;
    map[1].position[2] = 0.0f;
    map[1].color[0] = 1.0f;
    map[1].color[1] = 1.0f;
    map[1].color[2] = 0.0f;

    map[2].position[0] = 1.0f;
    map[2].position[1] = 0.0f;
    map[2].position[2] = 0.0f;
    map[2].color[0] = 0.0f;
    map[2].color[1] = 1.0f;
    map[2].color[2] = 0.0f;

    map[3].position[0] = 1.0f;
    map[3].position[1] = 1.0f;
    map[3].position[2] = 0.0f;
    map[3].color[0] = 0.0f;
    map[3].color[1] = 1.0f;
    map[3].color[2] = 1.0f;
  }

  glUnmapBuffer(GL_ARRAY_BUFFER);
  assert (glGetError() == GL_NO_ERROR);

  glGenBuffers(2, triangles);
  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, triangles[0]);
  glBufferData(GL_ELEMENT_ARRAY_BUFFER, 3, NULL, GL_STREAM_DRAW);

  {
    unsigned char *map = glMapBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_WRITE_ONLY);
    assert (map != NULL);
    map[0] = 0;
    map[1] = 1;
    map[2] = 2;
  }
  glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);

  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, triangles[1]);
  glBufferData(GL_ELEMENT_ARRAY_BUFFER, 3, NULL, GL_STREAM_DRAW);

  {
    unsigned char *map = glMapBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_WRITE_ONLY);
    assert (map != NULL);
    map[0] = 0;
    map[1] = 2;
    map[2] = 3;
  }
  glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);

  assert(glGetError() == GL_NO_ERROR);
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
  glutCreateWindow("VBO");
  init_data();
  glutReshapeFunc(reshape);
  glutDisplayFunc(display);
  glutIdleFunc(glutPostRedisplay);

  glutMainLoop();
  return 0;
}

