
#include "MyLayoutAnalysis.h"

namespace transkribus {


MyLayoutAnalysis::MyLayoutAnalysis() {}

void MyLayoutAnalysis::process(Image& image, const std::string& xmlInOut, const std::vector<std::string>& ids, const std::vector<std::string>& props) {
	std::cout << "MyLayoutAnalysis 1, process, xmlInOut =  " << xmlInOut << std::endl;
}

std::string MyLayoutAnalysis::usage() {
	return std::string("MyUsage");
}

std::string MyLayoutAnalysis::getToolName() {
	return std::string("MyToolName");
}

std::string MyLayoutAnalysis::getVersion() {
	return std::string("MyVersion");
}

std::string MyLayoutAnalysis::getProvider() {
	return std::string("MyProvider");
}

}
