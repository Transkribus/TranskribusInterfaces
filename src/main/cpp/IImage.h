
#pragma once

#include <iostream>
#include <stdexcept>
#include <opencv2/opencv.hpp>

namespace transkribus {

class IImage
{
private:
	std::string url;
	cv::Mat mat;

public:
	IImage(const std::string& url) : url(url) {}
	IImage(cv::Mat& mat) : mat(mat) {}




	virtual ~Image() { }
};

}
