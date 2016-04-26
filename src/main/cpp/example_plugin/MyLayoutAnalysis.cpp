
#include "MyLayoutAnalysis.h"

namespace transkribus {


MyLayoutAnalysis::MyLayoutAnalysis() {}

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

}
