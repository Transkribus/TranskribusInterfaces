
#pragma once

#include <iostream>
#include "Image.h"

namespace transkribus_interfaces {

class ILayoutAnalysis
{
public:
	ILayoutAnalysis() {}
	virtual ~ILayoutAnalysis() {}

    virtual bool processLayout(Image image, const std::string& xmlFileIn, const std::string& xmlFileOut) {}

    virtual bool process(Image image, const std::string& xmlFileIn, const std::string& xmlFileOut, std::vector<std::string>& ids, std::vector<std::string>& props) {}
};

}
