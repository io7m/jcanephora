
#define GL_GLEXT_PROTOTYPES 1

#include <assert.h>
#include <err.h>
#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>
#include <GL/glut.h>
#include <GL/glext.h>

typedef float vector3[3];

static GLuint buffer_pos;
static GLuint buffer_rgb;
static GLuint triangles[2];

static void
init_data()
{
  unsigned int elements = 4;
  unsigned int size = elements * sizeof(vector3);

  glGenBuffers(1, &buffer_pos);
  glBindBuffer(GL_ARRAY_BUFFER, buffer_pos);
  glBufferData(GL_ARRAY_BUFFER, size, NULL, GL_STREAM_DRAW);
  assert (glGetError() == GL_NO_ERROR);

  {
    vector3 *map = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY);
    assert (map != NULL);

    map[0][0] = -0.5f;
    map[0][1] = 0.5f;
    map[0][2] = 0.0f;

    map[1][0] = -0.5f;
    map[1][1] = -0.5f;
    map[1][2] = 0.0f;

    map[2][0] = 0.5f;
    map[2][1] = -0.5f;
    map[2][2] = 0.0f;

    map[3][0] = 0.5f;
    map[3][1] = 0.5f;
    map[3][2] = 0.0f;

    glUnmapBuffer(GL_ARRAY_BUFFER);
    assert (glGetError() == GL_NO_ERROR);
  }

  glGenBuffers(1, &buffer_rgb);
  glBindBuffer(GL_ARRAY_BUFFER, buffer_rgb);
  glBufferData(GL_ARRAY_BUFFER, size, NULL, GL_STREAM_DRAW);
  assert (glGetError() == GL_NO_ERROR);

  {
    vector3 *map = glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY);
    assert (map != NULL);

    map[0][0] = 1.0f;
    map[0][1] = 0.0f;
    map[0][2] = 0.0f;

    map[1][0] = 0.0f;
    map[1][1] = 1.0f;
    map[1][2] = 0.0f;

    map[2][0] = 0.0f;
    map[2][1] = 0.0f;
    map[2][2] = 1.0f;

    map[3][0] = 1.0f;
    map[3][1] = 1.0f;
    map[3][2] = 0.0f;

    glUnmapBuffer(GL_ARRAY_BUFFER);
    assert (glGetError() == GL_NO_ERROR);
  }

  glGenBuffers(2, triangles);
  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, triangles[0]);
  glBufferData(GL_ELEMENT_ARRAY_BUFFER, 3, NULL, GL_STREAM_DRAW);

  {
    unsigned char *map = glMapBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_WRITE_ONLY);
    assert (map != NULL);
    map[0] = 0;
    map[1] = 1;
    map[2] = 2;
    glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
  }

  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, triangles[1]);
  glBufferData(GL_ELEMENT_ARRAY_BUFFER, 3, NULL, GL_STREAM_DRAW);

  {
    unsigned char *map = glMapBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_WRITE_ONLY);
    assert (map != NULL);
    map[0] = 0;
    map[1] = 2;
    map[2] = 3;
    glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
  }

  assert(glGetError() == GL_NO_ERROR);
}

static void
display(void)
{
  glClearColor(0.2, 0.2, 0.2, 1.0);
  glClear(GL_COLOR_BUFFER_BIT);

  glBindBuffer(GL_ARRAY_BUFFER, buffer_pos);
  glEnableClientState(GL_VERTEX_ARRAY);
  glVertexPointer(3, GL_FLOAT, sizeof(vector3), NULL);

  glBindBuffer(GL_ARRAY_BUFFER, buffer_rgb);
  glEnableClientState(GL_COLOR_ARRAY);
  glColorPointer(3, GL_FLOAT, sizeof(vector3), NULL);

  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, triangles[0]);
  glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_BYTE, NULL);
  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, triangles[1]);
  glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_BYTE, NULL);

  glutSwapBuffers();
}

int
main (int argc, char **argv)
{
  glutInit(&argc, argv);
  glutInitDisplayMode(GLUT_DOUBLE);
  glutInitWindowSize(256, 256);
  glutCreateWindow("VBO");

  init_data();
  glutDisplayFunc(display);
  glutIdleFunc(glutPostRedisplay);
  glutMainLoop();
  return 0;
}

