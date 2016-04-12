
#pragma once

#include <iostream>
#include "Image.h"
#include "IModule.h"

namespace transkribus {

class ILayoutAnalysis : public IModule
{
public:
	virtual ~ILayoutAnalysis() {}
    virtual void process(Image& image, const std::string& xmlInOut, const std::vector<std::string>& ids, const std::vector<std::string>& props) = 0;
};

}
