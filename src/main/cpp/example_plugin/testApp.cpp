#include <iostream>
#include <stdexcept>
#include <dlfcn.h>
#include "../ILayoutAnalysis.h"
#include "../ModuleFactory.h"

using namespace transkribus;
using namespace std;

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

int main(int argc, char** argv) {
test();
test();

}
