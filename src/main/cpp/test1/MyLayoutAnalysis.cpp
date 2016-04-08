
#include "MyLayoutAnalysis.h"

namespace transkribus_interfaces {


MyLayoutAnalysis::MyLayoutAnalysis() {
}

bool MyLayoutAnalysis::processLayout(Image& image, const std::string& xmlFileIn,
		const std::string& xmlFileOut) {
	std::cout << "processLayout " << xmlFileIn << " " << xmlFileOut;
	return true;
}

bool MyLayoutAnalysis::process(Image& image, const std::string& xmlFileIn,
		const std::string& xmlFileOut, std::vector<std::string>& ids,
		std::vector<std::string>& props) {
	std::cout << "MyLayoutAnalysis 1, process " << xmlFileIn << " "
			<< xmlFileOut;
	return true;
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
