
#pragma once

#include <iostream>
#include "Image.h"
#include "IModuleDescription.h"

namespace transkribus_interfaces {

class ILayoutAnalysis : public IModuleDescription
{
public:
	//ILayoutAnalysis() {}
	virtual ~ILayoutAnalysis() {}

    virtual bool processLayout(Image& image, const std::string& xmlFileIn, const std::string& xmlFileOut) = 0;

    virtual bool process(Image& image, const std::string& xmlFileIn, const std::string& xmlFileOut, std::vector<std::string>& ids, std::vector<std::string>& props) = 0;
};

}
