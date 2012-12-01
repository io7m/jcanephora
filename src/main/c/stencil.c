#define GL_GLEXT_PROTOTYPES 1

#include <assert.h>
#include <err.h>
#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>
#include <GL/glut.h>
#include <GL/glext.h>

#define BOX_COUNT 256
#define BOX_WIDTH 32
#define BOX_HEIGHT 32
#define WINDOW_WIDTH 640
#define WINDOW_HEIGHT 480

struct box_t {
  double position[2];
  double velocity[2];
};

static void
box_move (struct box_t *b)
{
  b->position[0] += b->velocity[0];
  b->position[1] += b->velocity[1];

  if (b->position[0] < 0)
    b->velocity[0] = -b->velocity[0];
  if (b->position[0] + BOX_WIDTH  > WINDOW_WIDTH)
    b->velocity[0] = -b->velocity[0];

  if (b->position[1] < 0)
    b->velocity[1] = -b->velocity[1];
  if (b->position[1] + BOX_HEIGHT > WINDOW_HEIGHT)
    b->velocity[1] = -b->velocity[1];
}

static void
box_draw (const struct box_t *b)
{
  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();

  glTranslated(b->position[0], b->position[1], 0.0);
  glBegin(GL_QUADS);
  {
    glColor3d(1.0f, 0.0f, 0.0f);
    glVertex3d(0.0f, BOX_HEIGHT, -1.0f);
    glColor3d(0.0f, 1.0f, 0.0f);
    glVertex3d(0.0f, 0.0f, -1.0f);
    glColor3d(0.0f, 0.0f, 1.0f);
    glVertex3d(BOX_WIDTH, 0.0f, -1.0f);
    glColor3d(1.0f, 1.0f, 1.0f);
    glVertex3d(BOX_WIDTH, BOX_HEIGHT, -1.0f);
  }
  glEnd();
}

static struct box_t boxes[BOX_COUNT];

static void
display(void)
{
  int index;

  glClearColor(0.2, 0.2, 0.2, 1.0);
  glClearStencil(0);
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  glOrtho(0, WINDOW_WIDTH, 0, WINDOW_HEIGHT, 1, 100);

  glEnable(GL_STENCIL_TEST);
  glColorMask(0, 0, 0, 0);
  glStencilFunc(GL_ALWAYS, 1, 0xFF);
  glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
  glStencilMask(0xFF);

  for (index = 0; index < WINDOW_WIDTH; index += 20) {
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    glTranslated(index, 0, 0);

    glBegin(GL_QUADS);
    {
      glVertex3d(0.0f, WINDOW_HEIGHT, -1.0f);
      glVertex3d(0.0f, 0.0f, -1.0f);
      glVertex3d(10, 0.0f, -1.0f);
      glVertex3d(10, WINDOW_HEIGHT, -1.0f);
    }
    glEnd();
  }

  glColorMask(1, 1, 1, 1);
  glStencilFunc(GL_EQUAL, 1, 0xFF);
  glStencilMask(0);

  for (index = 0; index < BOX_COUNT; ++index) {
    box_move(&boxes[index]);
    box_draw(&boxes[index]);
  }

  glutSwapBuffers();
}

int
main (int argc, char **argv)
{
  int index;

  for (index = 0; index < BOX_COUNT; ++index) {
    boxes[index].position[0] = random() % (WINDOW_WIDTH - BOX_WIDTH);
    boxes[index].position[1] = random() % (WINDOW_HEIGHT - BOX_HEIGHT);
    boxes[index].velocity[0] = (random() % 8) - 2;
    boxes[index].velocity[1] = (random() % 8) - 2;
  }

  glutInit(&argc, argv);
  glutInitDisplayMode(GLUT_DOUBLE | GLUT_STENCIL);
  glutInitWindowSize(WINDOW_WIDTH, WINDOW_HEIGHT);
  glutCreateWindow("Stencil");

  glutDisplayFunc(display);
  glutIdleFunc(glutPostRedisplay);
  glutMainLoop();
  return 0;
}

