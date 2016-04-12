
#pragma once

#include <iostream>
#include <stdexcept>
#include "opencv2/core/core.hpp"

namespace transkribus {

class Image
{
private:
	std::string url;
	cv::Mat mat;

public:
	Image(const std::string& url);
	Image(cv::Mat& mat);

	virtual ~Image() { }
};

}
