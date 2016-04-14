
FILE = loadAndDisplayImage.cpp

$(FILE).o: $(FILE)
	g++ -Wall $(FILE) -ldl -lopencv_core -lopencv_imgcodecs -lopencv_highgui -lcurl -o $(FILE).o
	
test: $(FILE).o
	./$(FILE).o
	
clean:
	rm -f $(FILE).o