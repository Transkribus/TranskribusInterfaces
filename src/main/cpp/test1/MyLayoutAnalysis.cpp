

#include "MyLayoutAnalysis.h"

namespace transkribus_interfaces {

	bool MyLayoutAnalysis::process(Image image, const std::string& xmlFileIn, const std::string& xmlFileOut, std::vector<std::string>& ids, std::vector<std::string>& props) {
		std::cout << "MyLayoutAnalysis 1, process " << xmlFileIn << " " << xmlFileOut;
		return true;
	}

}
