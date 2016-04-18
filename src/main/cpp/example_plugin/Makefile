
SRCS = MyLayoutAnalysis.cpp
HDRS = MyLayoutAnalysis.h

LIB = libMyLayoutAnalysis.so
CC_FLAGS = -Wall -std=gnu++11 -fPIC
LD_FLAGS = -L../../resources -ldl -lTranskribusInterfaces -lopencv_core -lopencv_imgcodecs -lopencv_highgui -lcurl

#all: $(LIB) copy_to_resources
#all: $(LIB) copy_to_resources

$(LIB): $(SRCS) $(HDRS)
	g++ $(CC_FLAGS) -shared $(SRCS) -o $(LIB)
	
testApp: testApp.cpp $(LIB)
	g++ $(CC_FLAGS) testApp.cpp -o testApp -lMyLayoutAnalysis $(LD_FLAGS)
	
test: ./testApp
	cp ../../resources/libTranskribusInterfaces.so .
	./testApp
	
#copy_to_resources: $(LIB)
#	cp $(LIB) ../../resources/.
	
clean:
	rm -f $(LIB) ./testApp