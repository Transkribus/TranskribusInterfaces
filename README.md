# TranskribusInterfaces
Interfaces for the Transkribus modules like layout analysis, htr etc.

Directory layout:

src/main/java/ -> java interfaces

src/main/cpp -> c++ interfaces

src/main/swig -> SWIG wrapper implementation for C++ to Java

src/main/resources -> shared objects, config files etc.

src/test/java -> some java test code

src/test/cpp -> some c++ test code

[![Build Status](http://dbis-halvar.uibk.ac.at/jenkins/buildStatus/icon?job=TranskribusInterfaces)](http://dbis-halvar.uibk.ac.at/jenkins/job/TranskribusInterfaces)

## Building
Here is a short guide with steps that need to be performed
to build your project.	

### Requirements
- Java >= version 7
- OpenCV 3.10
- Maven
- All further dependencies are gathered via Maven

### Build Steps
```
git clone https://github.com/Transkribus/TranskribusInterfaces
cd TranskribusInterfaces
mvn install
```
- To generate the Java wrapper files for the C++ interfaces, switch to the src/main/swig directory and type make
- Wrappers will be generated in the java package eu.transkribus.interfaces.native_wrapper.swig
- Proxy classes that implement a Java interface and communicate with the respective C++ interfaces can be found in  eu.transkribus.interfaces.native_wrapper
- C++ modules should be implemented in plugin fashion as outlined with an example in the src/main/cpp/example_plugin directory
- Java modules should be implemented in plugin fashion as outlined with an example in the eu.transkribus.interfaces.example_module package


### Links
- https://transkribus.eu/TranskribusInterfaces/apidocs/index.html
