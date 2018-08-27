
CPP_DIR=src/main/cpp
SWIG_DIR=src/main/swig

all: clean cpp swig mvn
wrapper: cpp swig

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
mvn_wot:
	mvn clean install -Dmaven.test.skip=true	

clean: clean_cpp clean_swig
	rm src/main/resources/*.so
install: 
	cp src/main/resources/*.so /usr/local/lib/.
