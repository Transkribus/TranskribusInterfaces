#include <iostream>
#include <stdexcept>
#include <dlfcn.h>
#include "../ILayoutAnalysis.h"

using namespace transkribus_interfaces;
using namespace std;

void* loadLibrary(std::string& libName) {
	void* library_handle = dlopen(libName.c_str(), RTLD_NOW | RTLD_GLOBAL);

	if (!library_handle) {
		throw runtime_error("cannot load library "+libName+" error: "+dlerror());
	}

	return library_handle;
}

void test() {
	void* library_handle;
	LayoutAnalysisFactory* factory;
	ILayoutAnalysis* la;

	std::string libName("./libMyLayoutAnalysis.so");

	cout << "opening lib: " << libName << endl;

	try {
		library_handle = loadLibrary(libName);
	} catch (runtime_error& e) {
		cerr << e.what() << endl;
		exit(EXIT_FAILURE);
	}

//	library_handle = dlopen(libName, RTLD_NOW | RTLD_GLOBAL);
//	if (!library_handle) {
//		cerr << "dlopen failed for " << libName << ", error: " << dlerror() << endl;
//		exit(EXIT_FAILURE);
//	}

	factory = (LayoutAnalysisFactory*) dlsym(library_handle, "Factory");
	if (factory == NULL) {
		cerr << "error getting factory" << endl;
		exit(1);
	}

	la = factory->create();

	Image image("myurl");
	vector<string> pars1;
	vector<string> pars2;

	la->process(image, "str1", "str2", pars1, pars2);

}

int main(int argc, char** argv) {
test();
test();

}
