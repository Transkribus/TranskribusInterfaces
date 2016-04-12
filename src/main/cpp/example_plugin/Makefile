
SRCS = MyLayoutAnalysis.cpp
HDRS = MyLayoutAnalysis.h

LIB = libMyLayoutAnalysis.so

TESTAPP = testApp

all: $(LIB) copy_to_resources

$(LIB): $(SRCS) $(HDRS)
	g++ -Wall -fPIC -shared $(SRCS) -o $(LIB)
	
$(TESTAPP): $(TESTAPP).cpp
	g++ -Wall $(TESTAPP).cpp -ldl -lopencv_core -o $(TESTAPP)
	
test: $(TESTAPP)
	./$(TESTAPP)
	
copy_to_resources: $(LIB)
	cp $(LIB) ../../resources/.
	
clean:
	rm -f $(LIB) $(TESTAPP)