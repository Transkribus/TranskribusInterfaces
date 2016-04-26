
#include "MyLayoutAnalysis.h"
#include "../Image.h"

#include <iostream>

namespace transkribus {


MyLayoutAnalysis::MyLayoutAnalysis() {}

MyLayoutAnalysis::~MyLayoutAnalysis()
{
	std::cout << "MyLayoutAnalysis is deleted!" << std::endl;
}

void MyLayoutAnalysis::process(Image& image, const std::string& xmlInOut, const std::vector<std::string>& ids, const std::vector<std::string>& props) {
	std::cout << "MyLayoutAnalysis, process, xmlInOut =  " << xmlInOut << std::endl;
	std::cout << image << std::endl;
}

const std::string MyLayoutAnalysis::usage() const {
	return std::string("MyUsage");
}

const std::string MyLayoutAnalysis::getToolName() const {
	return std::string("MyToolName");
}

const std::string MyLayoutAnalysis::getVersion() const {
	return std::string("MyVersion");
}

const std::string MyLayoutAnalysis::getProvider() const {
	return std::string("MyProvider");
}


IModule * MyLayoutAnalysisModuleFactory::create(const std::vector<std::string>& pars)
{
	std::cout << "creating MyLayoutAnalysis" << std::endl;
	return new MyLayoutAnalysis;
}

}
