CC=cc
CFLAGS=-g -W -Wall -Werror -pedantic -std=c90
LDFLAGS=-lglut -lGL -ltiff

all: vbo texture-use

vbo: vbo.c
	${CC} ${CFLAGS} -o vbo vbo.c ${LDFLAGS}

texture-use: texture-use.c
	${CC} ${CFLAGS} -o texture-use texture-use.c ${LDFLAGS}

clean:
	rm -f vbo texture-use

