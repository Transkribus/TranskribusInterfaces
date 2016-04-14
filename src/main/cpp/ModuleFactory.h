
#pragma once

//#include <iostream>
#include <string>
#include <vector>
#include "IBaseLine2Coords.h"
#include "ILayoutAnalysis.h"
#include "IHtr.h"

using namespace std;

namespace transkribus {

class ModuleFactory
{
public:
	virtual ~ModuleFactory() {}

	virtual IModule* create(const vector<string>& pars) { return NULL; }

	static IModule* createModuleFromLib(const string& pathToLib, const std::vector<std::string>& pars);

	// casting methods, needed for the java wrapper:

	static ILayoutAnalysis* castToLayoutAnalysis(IModule* module) {
		return (ILayoutAnalysis *) module;
	}

	static IHtr* castToHtr(IModule* module) {
		return (IHtr *) module;
	}

	static IBaseline2Coords* castToBaseline2Coords(IModule* module) {
		return (IBaseline2Coords *) module;
	}

private:
	static const string FACTORY_VARIABLE_NAME;
	static void* loadLibrary(const string& pathToLib);

};

}
