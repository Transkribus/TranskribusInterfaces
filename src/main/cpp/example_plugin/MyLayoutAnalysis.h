


#pragma once

#include <iostream>
#include "../ILayoutAnalysis.h"
#include "../ModuleFactory.h"

using namespace std;

namespace transkribus {

// 1st step: Create and implementation of your module, inherited from the corresponding interface class
class MyLayoutAnalysis : public ILayoutAnalysis
{
public:
	MyLayoutAnalysis();

	virtual ~MyLayoutAnalysis() {
		cout << "MyLayoutAnalysis is deleted!" << endl;
	}

    void process(Image& image, const std::string& xmlInOut, const std::vector<std::string>& ids, const std::vector<std::string>& props);

    std::string usage();

    std::string getToolName();

    std::string getVersion();

    std::string getProvider();
};

// 2nd step: for every module, you have to implement a ModuleFactory, which creates an instance of your module with some parameters
class MyLayoutAnalysisModuleFactory : public ModuleFactory
{
public:
	IModule* create(const vector<string>& pars) {
		std::cout << "creating MyLayoutAnalysis" << std::endl;
		return new MyLayoutAnalysis;
	}
};

}

// 3rd step, IMPORTANT: you have to declare an instance of your factory with the name 'ModuleFactoryInstance' outside of any namespace!
// This enables us to load the shared lib with dlopen and access the factory instance without and mangled name issues
transkribus::MyLayoutAnalysisModuleFactory ModuleFactoryInstance;


