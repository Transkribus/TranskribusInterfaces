
#pragma once

//#include <iostream>
#include <string>
#include <vector>
#include "IBaseline2Polygon.h"
#include "ILayoutAnalysis.h"
#include "IHtr.h"
#include "ITrainHtr.h"

using namespace std;

namespace transkribus {

class ModuleFactory
{
public:
	static const string FACTORY_VARIABLE_NAME;

	virtual ~ModuleFactory() {}

	virtual IModule* create(const vector<string>& pars) { return NULL; }

	static IModule* createModuleFromLib(const string& pathToLib, const std::vector<std::string>& pars);

	// casting methods, needed for the swig java wrapper:

	static ILayoutAnalysis* castILayoutAnalysis(IModule* module) {
		return (ILayoutAnalysis *) module;
	}

	static IHtr* castIHtr(IModule* module) {
		return (IHtr *) module;
	}

	static IBaseline2Polygon* castIBaseline2Coords(IModule* module) {
		return (IBaseline2Polygon *) module;
	}

	static ITrainHtr* castITrainHtr(IModule* module) {
		return (ITrainHtr *) module;
	}

private:
	static void* loadLibrary(const string& pathToLib);

};

}
