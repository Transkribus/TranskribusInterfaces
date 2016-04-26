
#pragma once

#include "IModule.h"

namespace transkribus {

class Image;

class IBaseline2Polygon : public IModule
{
public:
	virtual ~IBaseline2Polygon() {}

	virtual void process(Image& image, const std::string& xmlInOut, const std::vector<std::string>& ids, const std::vector<std::string>& props) = 0;
};

}
