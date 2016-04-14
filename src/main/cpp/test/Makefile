
CC_FLAGS = -Wall -std=gnu++11
LD_FLAGS = -ldl -lopencv_core -lopencv_imgcodecs -lopencv_highgui -lcurl

CPP_FILES = ../ImageUtils.cpp ../Image.cpp
HEADER_FILES = ../ImageUtils.h ../Image.h 
OBJ_FILES = ImageUtils.o Image.o

all: test

test: main.o
	./main.o

main.o: $(OBJ_FILES) main.cpp
	g++ $(CC_FLAGS) main.cpp -o $@ $(OBJ_FILES) $(LD_FLAGS)
	
%.o: ../%.cpp ../%.h
	g++ $(CC_FLAGS) -c -o $@ $<
	
clean:
	rm -f $(OBJ_FILES) main.o