
#pragma once

#include <iostream>
#include <stdexcept>
#include "opencv2/core/core.hpp"

namespace transkribus_interfaces {

class Image
{
private:
	std::string url;
	cv::Mat mat;

public:
	Image(const std::string& url) : url(url) {
		std::cout << "hello url " << url << std::endl;
	}

	void doSthWithException() {
		throw std::runtime_error("exception!!");
	}

	Image(cv::Mat& mat) : mat(mat) {
		std::cout << "hello mat, w = " << mat.cols << " h = " << mat.rows << std::endl;
	}

	virtual ~Image() {


	}

};

}
