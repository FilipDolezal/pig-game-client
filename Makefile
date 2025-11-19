.PHONY: all run clean

all: run

run:
	cmake -S . -B build
	cmake --build build --target run

clean:
	cmake --build build --target project-clean
