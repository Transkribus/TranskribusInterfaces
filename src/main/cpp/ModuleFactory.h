
#pragma once

#include <string>
#include <vector>

//// diem: remove 
//#include "IBaseline2Polygon.h"
//#include "ILayoutAnalysis.h"
//#include "IHtr.h"
//#include "ITrainHtr.h"

// define the export
#ifndef TiExport

//  exports for windows
#ifdef TI_DLL_EXPORT
#define TiExport __declspec(dllexport)
#else
#define TiExport	// remove
#endif
#endif


namespace transkribus {

// forward declarations
class IModule; 
class ILayoutAnalysis;
class IHtr;
class IBaseline2Polygon;
class ITrainHtr;

class TiExport ModuleFactory
{
public:
	static const std::string FACTORY_VARIABLE_NAME;

	virtual ~ModuleFactory() {}

	virtual IModule* create(const std::vector<std::string>& pars) { return NULL; };

	static IModule* createModuleFromLib(const std::string& pathToLib, const std::vector<std::string>& pars);

	// casting methods, needed for the swig java wrapper:

	static ILayoutAnalysis* castILayoutAnalysis(IModule* module);

	static IHtr* castIHtr(IModule* module);

	static IBaseline2Polygon* castIBaseline2Coords(IModule* module);

	static ITrainHtr* castITrainHtr(IModule* module);

private:
	static void* loadLibrary(const std::string& pathToLib);

};

}
