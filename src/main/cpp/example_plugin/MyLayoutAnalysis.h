
#pragma once

#include <iostream>
#include "../ILayoutAnalysis.h"		// diem: the include directory should be added to the project rather than using relative includes
#include "../ModuleFactory.h"

namespace transkribus {

// 1st step: Create and implementation of your module, inherited from the corresponding interface class
class MyLayoutAnalysis : public ILayoutAnalysis
{
public:
	MyLayoutAnalysis();

	virtual ~MyLayoutAnalysis() {
		std::cout << "MyLayoutAnalysis is deleted!" << std::endl;
	}

    void process(Image& image, const std::string& xmlInOut, const std::vector<std::string>& ids, const std::vector<std::string>& props);

    const std::string usage() const;

    const std::string getToolName() const;

    const std::string getVersion() const;

    const std::string getProvider() const;
};

// 2nd step: for every module, you have to implement a ModuleFactory, which creates an instance of your module with some parameters
class TiExport MyLayoutAnalysisModuleFactory : public ModuleFactory
{
public:
	IModule* create(const std::vector<std::string>& pars) {
		std::cout << "creating MyLayoutAnalysis" << std::endl;
		return new MyLayoutAnalysis;
	}

};

}

// 3rd step, IMPORTANT: you have to declare an instance of your factory with the name 'ModuleFactoryInstance' outside of any namespace!
// This enables us to load the shared lib with dlopen and access the factory instance without and mangled name issues
extern "C" {
	TiExport transkribus::MyLayoutAnalysisModuleFactory ModuleFactoryInstance;
}
