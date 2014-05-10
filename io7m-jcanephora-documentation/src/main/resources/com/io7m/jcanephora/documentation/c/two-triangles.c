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
  vertex *map;

  /**
   * Allocate _size_ bytes in and populate them with vertex data.
   */

  map = malloc(size);
  if (map == NULL) {
    err(1, "malloc");
  }

  {
    map[0].position[0] = -0.5f;
    map[0].position[1] = 0.5f;
    map[0].position[2] = 0.0f;
    map[0].color[0] = 1.0f;
    map[0].color[1] = 0.0f;
    map[0].color[2] = 0.0f;

    map[1].position[0] = -0.5f;
    map[1].position[1] = -0.5f;
    map[1].position[2] = 0.0f;
    map[1].color[0] = 1.0f;
    map[1].color[1] = 1.0f;
    map[1].color[2] = 0.0f;

    map[2].position[0] = 0.5f;
    map[2].position[1] = -0.5f;
    map[2].position[2] = 0.0f;
    map[2].color[0] = 0.0f;
    map[2].color[1] = 1.0f;
    map[2].color[2] = 0.0f;

    map[3].position[0] = 0.5f;
    map[3].position[1] = 0.5f;
    map[3].position[2] = 0.0f;
    map[3].color[0] = 0.0f;
    map[3].color[1] = 1.0f;
    map[3].color[2] = 1.0f;
  }

  /**
   * Allocate an array buffer and upload the vertex data.
   */

  glGenBuffers(1, &buffer);
  glBindBuffer(GL_ARRAY_BUFFER, buffer);
  glBufferData(GL_ARRAY_BUFFER, size, map, GL_STATIC_DRAW);
  assert (glGetError() == GL_NO_ERROR);
  free(map);

  /**
   * Allocate two index buffers, and upload index data.
   */

  glGenBuffers(2, triangles);
  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, triangles[0]);

  {
    unsigned char map[3];
    map[0] = 0;
    map[1] = 1;
    map[2] = 2;
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, 3, map, GL_STATIC_DRAW);
  }

  glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, triangles[1]);

  {
    unsigned char map[3];
    map[0] = 0;
    map[1] = 2;
    map[2] = 3;
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, 3, map, GL_STATIC_DRAW);
  }

  assert(glGetError() == GL_NO_ERROR);
}

static void
display(void)
{
  glClearColor(0.2, 0.2, 0.2, 1.0);
  glClear(GL_COLOR_BUFFER_BIT);

  glBindBuffer(GL_ARRAY_BUFFER, buffer);
  glEnableClientState(GL_VERTEX_ARRAY);
  glVertexPointer(3, GL_FLOAT, sizeof(vertex), NULL);
  glEnableClientState(GL_COLOR_ARRAY);
  glColorPointer(3, GL_FLOAT, sizeof(vertex), (void *) offsetof(vertex, color));

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

