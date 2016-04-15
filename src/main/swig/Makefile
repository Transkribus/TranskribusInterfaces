# Make sure SWIG is installed and the swig binary is available!

MAIN_MAKEFILE = Makefile.Main
SWIG       = swig
#CXXSRCS    = ../cpp/ModuleFactory.cpp ../cpp/Image.cpp
TARGET     = TranskribusInterfacesWrapper
INTERFACE  = transkribus_interfaces.i

SWIG_OUTDIR = ../java/eu/transkribus/interfaces/native_wrapper/swig
JAVA_PACKAGE = eu.transkribus.interfaces.native_wrapper.swig

JAVA_INCLUDE = -I"." -I"$(JAVA_HOME)/include"

SWIGOPT    = -outdir $(SWIG_OUTDIR) -package $(JAVA_PACKAGE)
#SWIGOPT    = -package $(JAVA_PACKAGE)

#SWIGOPT    = 
#JAVASRCS   = *.java
#RUNME	   = runme

#CXXFLAGS   = -I./catti-plugin -g3 -fPIC # -std=c++11
#CXXFLAGS   = -Wall -g3 -fPIC -L. -std=c++11
CXXFLAGS   = -Wall -fPIC -std=c++11 

#LIBS = -lMyLayoutAnalysis -lopencv_core
#LIBS = -lopencv_core
LDFLAGS = 
#LIBS = -L. -L../cpp -ldl -lopencv_core -lopencv_imgcodecs -lopencv_highgui -lcurl

INTERFACES_LIB = TranskribusInterfaces
LIBS = -L. -L../cpp -l$(INTERFACES_LIB)

#all:	plugin java
all:	plugin copy_to_resources

# create java wrappers in SWIG_OUTDIR and a shared library that can be loaded in java to access the C++ interfaces
plugin: $(CXXSRCS) ../cpp/libTranskribusInterfaces.so
	$(MAKE) -f $(MAIN_MAKEFILE) JAVA_INCLUDE="$(JAVA_INCLUDE)" SRCDIR='$(SRCDIR)' CXXSRCS='$(CXXSRCS)' SWIG='$(SWIG)' \
		SWIGOPT='$(SWIGOPT)' TARGET='$(TARGET)' INTERFACE='$(INTERFACE)' CXXFLAGS='$(CXXFLAGS)' LIBS='$(LIBS)' java_cpp
		
../cpp/lib$(INTERFACES_LIB).so:
	cd ../cpp && make
		
# copies the generated lib to src/main/resources
copy_to_resources: lib$(TARGET).so
	cp lib$(TARGET).so ../resources/.
		
#java:
#	$(MAKE) -f $(MAIN_MAKEFILE) SRCDIR='$(SRCDIR)' JAVASRCS='$(JAVASRCS)' JAVAFLAGS='$(JAVAFLAGS)' java_compile

#check:	
#	$(MAKE) -f $(MAIN_MAKEFILE) SRCDIR='$(SRCDIR)' java_run

clean:
	#rm -f *_wrap* *~ .~* *.class `find . -name \*.java | grep -v $(RUNME).java` #
	rm -f *_wrap* *~ .~* # remove cxx wrapper code and temporary files
	rm -f $(SWIG_OUTDIR)/*.java $(SWIG_OUTDIR)/*.class # removes swig java wrappers
	rm -f *.o lib$(TARGET).so # remove java wrapper lib and object files
