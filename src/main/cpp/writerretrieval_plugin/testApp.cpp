#include <iostream>
#include <stdexcept>
#include "../IWriterRetrieval.h"
#include "../ModuleFactory.h"
#include "../Image.h"

#ifndef WIN32
#include <dlfcn.h>
#endif

#ifdef _MSC_BUILD // define subsystem for MSVC
#pragma comment (linker, "/SUBSYSTEM:CONSOLE")
#endif

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
		throw std::runtime_error("cannot load library "+libName+" error: "+dlerror());
	}

	return library_handle;
}

void test() {
	transkribus::ILayoutAnalysis* la;

	std::string libName("./libMyLayoutAnalysis.so");
	std::cout << "opening lib: " << libName << std::endl;

	transkribus::Image image("/tmp/test.jpg");

	std::vector<std::string> constructorPars;
	transkribus::IModule* module = transkribus::ModuleFactory::createModuleFromLib(libName, constructorPars);
	la = transkribus::ModuleFactory::castILayoutAnalysis(module);

	std::vector<std::string> ids;
	std::vector<std::string> props;
	la->process(image, "pageXmlFileUrl", ids, props);

}

#else

void test() {

	transkribus::IWriterRetrieval* wr;

	std::string libName("MyWriterRetrieval.dll");

	transkribus::Image image("C:/temp/test.jpg");

	std::vector<std::string> constructorPars;
	try {
		transkribus::IModule* module = transkribus::ModuleFactory::createModuleFromLib(libName, constructorPars);
		wr = transkribus::ModuleFactory::castIWriterRetrieval(module);

		std::vector<std::string> ids;
		std::vector<std::string> props;
		std::string feature = wr->process(image, "pageXmlFileUrl", ids, props);

		std::cout << "writer retrieval feature:" << std::endl;
		std::cout << feature << std::endl;

		delete module;	// diem: the module has to be deleted after use!
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
