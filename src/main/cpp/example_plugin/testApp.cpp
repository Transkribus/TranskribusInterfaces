#include <iostream>
#include <stdexcept>
#include "../ILayoutAnalysis.h"
#include "../ModuleFactory.h"

#ifndef WIN32
#include <dlfcn.h>
#endif

#ifdef _MSC_BUILD // define subsystem for MSVC
#pragma comment (linker, "/SUBSYSTEM:CONSOLE")
#endif

using namespace transkribus;
using namespace std;

// declare local functions
void* loadLibrary(std::string& libName);
void test();

int main(int argc, char** argv) {
	test();
	test();

	return 0;
}

#ifndef WIN32
void* loadLibrary(std::string& libName) {
	void* library_handle = dlopen(libName.c_str(), RTLD_NOW | RTLD_GLOBAL);

	if (!library_handle) {
		throw runtime_error("cannot load library "+libName+" error: "+dlerror());
	}

	return library_handle;
}

void test() {
	ILayoutAnalysis* la;

	std::string libName("./libMyLayoutAnalysis.so");
	cout << "opening lib: " << libName << endl;

	Image image("/tmp/test.jpg");

	vector<string> constructorPars;
	IModule* module = ModuleFactory::createModuleFromLib(libName, constructorPars);
	la = ModuleFactory::castILayoutAnalysis(module);

	vector<string> ids;
	vector<string> props;
	la->process(image, "pageXmlFileUrl", ids, props);

}

#else

void test() {

	ILayoutAnalysis* la;

	std::string libName("MyLayoutAnalysis.dll");

	Image image("C:/temp/test.jpg");

	vector<string> constructorPars;
	try {
		IModule* module = ModuleFactory::createModuleFromLib(libName, constructorPars);
		la = ModuleFactory::castILayoutAnalysis(module);

		vector<string> ids;
		vector<string> props;
		la->process(image, "pageXmlFileUrl", ids, props);
	}
	catch (std::exception e) {
		std::cout << "test failed: " << e.what() << std::endl;
	}
	catch(...) {
		std::cout << "unknown error: test failed..." << std::endl;
		return;
	}
	
}

#endif
