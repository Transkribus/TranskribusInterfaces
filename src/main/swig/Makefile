# Make sure SWIG is installed and the swig binary is available!

MAIN_MAKEFILE = Makefile.Main
SWIG       = swig
#CXXSRCS    = CattiSessionWrapper.cpp
CXXSRCS    = 
TARGET     = TranskribusInterfaces
INTERFACE  = transkribus_interfaces.i

SWIG_OUTDIR = ../java/eu/transkribus/native_interfaces
JAVA_PACKAGE = eu.transkribus.native_interfaces

SWIGOPT    = -outdir $(SWIG_OUTDIR) -package $(JAVA_PACKAGE)

#SWIGOPT    = 
JAVASRCS   = *.java
RUNME	   = runme
#CXXFLAGS   = -I./catti-plugin -g3 -fPIC # -std=c++11
CXXFLAGS   = -g3 -fPIC # -std=c++11

LIBS = -lMyLayoutAnalysis

#all:	plugin java
all:	plugin

check:	
	$(MAKE) -f $(MAIN_MAKEFILE) SRCDIR='$(SRCDIR)' java_run

plugin:
	$(MAKE) -f $(MAIN_MAKEFILE) SRCDIR='$(SRCDIR)' CXXSRCS='$(CXXSRCS)' SWIG='$(SWIG)' \
		SWIGOPT='$(SWIGOPT)' TARGET='$(TARGET)' INTERFACE='$(INTERFACE)' CXXFLAGS='$(CXXFLAGS)' LIBS='$(LIBS)' java_cpp

java:
	$(MAKE) -f $(MAIN_MAKEFILE) SRCDIR='$(SRCDIR)' JAVASRCS='$(JAVASRCS)' JAVAFLAGS='$(JAVAFLAGS)' java_compile

clean:
	rm -f *_wrap* *~ .~* *.class `find . -name \*.java | grep -v $(RUNME).java`
	rm -f $(SWIG_OUTDIR)/*.java $(SWIG_OUTDIR)/*.class  
	rm -f *.o lib$(TARGET).so
