CC=cc
CFLAGS=-g -W -Wall -Werror -pedantic -std=c90
LDFLAGS=-lglut -lGL -ltiff

all: draw vbo vbo2 texture-use

vbo: vbo.c
	${CC} ${CFLAGS} -o vbo vbo.c ${LDFLAGS}

vbo2: vbo2.c
	${CC} ${CFLAGS} -o vbo2 vbo2.c ${LDFLAGS}

texture-use: texture-use.c
	${CC} ${CFLAGS} -o texture-use texture-use.c ${LDFLAGS}

clean:
	rm -f draw vbo vbo2 texture-use

