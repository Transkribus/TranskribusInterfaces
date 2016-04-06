


#pragma once

#include <iostream>
#include "../ILayoutAnalysis.h"

namespace transkribus_interfaces {

class MyLayoutAnalysis : public ILayoutAnalysis
{
public:
    bool processLayout(Image image, const std::string& xmlFileIn, const std::string& xmlFileOut) {
    	std::cout << "MyLayoutAnalysis 1, processLayout " << xmlFileIn << " " << xmlFileOut;
    	return true;
    }

    bool process(Image image, const std::string& xmlFileIn, const std::string& xmlFileOut, std::vector<std::string>& ids, std::vector<std::string>& props);

	~MyLayoutAnalysis() {}
};

}
