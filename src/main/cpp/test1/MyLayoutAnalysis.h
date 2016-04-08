


#pragma once

#include <iostream>
#include "../ILayoutAnalysis.h"

namespace transkribus_interfaces {

class MyLayoutAnalysis : public ILayoutAnalysis
{
public:
	MyLayoutAnalysis();
	virtual ~MyLayoutAnalysis() {}

    bool processLayout(Image& image, const std::string& xmlFileIn, const std::string& xmlFileOut);

    bool process(Image& image, const std::string& xmlFileIn, const std::string& xmlFileOut, std::vector<std::string>& ids, std::vector<std::string>& props);

    std::string usage();

    std::string getToolName();

    std::string getVersion();

    std::string getProvider();


};

}
