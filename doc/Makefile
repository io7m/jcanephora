CC=cc
CFLAGS=-g -W -Wall -Werror -pedantic -std=c90
LDFLAGS=-lglut -lGL -ltiff

all: vbo textures texture-use

vbo: vbo.c
	${CC} ${CFLAGS} -o vbo vbo.c ${LDFLAGS}

textures: textures.c
	${CC} ${CFLAGS} -o textures textures.c ${LDFLAGS}

texture-use: texture-use.c
	${CC} ${CFLAGS} -o texture-use texture-use.c ${LDFLAGS}

clean:
	rm -f vbo textures texture-use

