
CPP_DIR=src/main/cpp
SWIG_DIR=src/main/swig

all: clean cpp swig mvn

cpp: 
	make -C $(CPP_DIR) all
clean_cpp: 
	make -C $(CPP_DIR) clean
swig:
	make -C $(SWIG_DIR) all
clean_swig: 
	make -C $(SWIG_DIR) clean
mvn: 
	mvn clean install
clean_mvn: 
	mvn clean

clean: clean_cpp clean_swig clean_mvn
	rm src/main/resources/*.so
install: 
	cp src/main/resources/*.so /usr/local/lib/.
