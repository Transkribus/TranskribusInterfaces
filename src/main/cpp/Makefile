
CC_FLAGS = -Wall -std=gnu++11 -fPIC
LIBS = -ldl -lopencv_core -lopencv_imgcodecs -lopencv_highgui -lcurl

LIB = libTranskribusInterfaces.so
LIBDIR = ./
LIBPATH = $(LIBDIR)$(LIB)
LIBPATH_RESOURCES = ../resources/$(LIB)

SRCS = ImageUtils.cpp Image.cpp ModuleFactory.cpp
OBJS = $(SRCS:.cpp=.o)

all: $(LIBPATH) $(LIBPATH_RESOURCES)

# creates the lib in the specified path
$(LIBPATH): $(OBJS)
	g++ $(CC_FLAGS) -shared $(OBJS) -o $(LIBPATH)

# this copies the library to the resources dir
$(LIBPATH_RESOURCES): $(LIBPATH)
	cp $(LIBPATH) $(LIBPATH_RESOURCES)

%.o: %.cpp %.h
	g++ $(CC_FLAGS) -c -o $@ $<
	
clean:
	rm -f *.o *.so