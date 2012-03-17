CC=cc
CFLAGS=-W -Wall -Werror -pedantic -std=c90
LDFLAGS=-lglut -lGL

all: vbo textures

vbo: vbo.c
	${CC} ${CFLAGS} -o vbo vbo.c ${LDFLAGS}

textures: textures.c
	${CC} ${CFLAGS} -o textures textures.c ${LDFLAGS}

clean:
	rm -f vbo textures

