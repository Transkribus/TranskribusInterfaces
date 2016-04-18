Interfaces for the Transkribus modules like layout analysis, htr etc.
-------------------------------------------------------------------------------

Directory layout:
	src/main/java/ -> java interfaces
	src/main/cpp -> c++ interfaces
	src/main/swig -> SWIG wrapper implementation for C++ to Java
	src/main/resources -> shared objects, config files etc.
	src/test/java -> some java test code
	src/test/cpp -> some c++ test code
	
- To generate the Java wrapper files for the C++ interfaces, switch to the src/main/swig directory and type make
- Wrappers will be generated in the java package eu.transkribus.interfaces.native_wrapper.swig
- Proxy classes that implement a Java interface and communicate with the respective C++ interfaces can be found in  eu.transkribus.interfaces.native_wrapper
- C++ modules should be implemented in plugin fashion as outlined with an example in the src/main/cpp/example_plugin directory
- Java modules should be implemented in plugin fashion as outlined with an example in the eu.transkribus.interfaces.example_module package