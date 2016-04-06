
FILE = MyLayoutAnalysis

all: lib$(FILE).so

lib$(FILE).so: $(FILE).h $(FILE).cpp
	g++ -Wall -fPIC -shared $(FILE).cpp -o lib$(FILE).so
	
clean:
	rm -f lib$(FILE).so