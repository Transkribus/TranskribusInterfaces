
#pragma once

//#include <iostream>
#include <ostream>
#include <string>
#include <stdexcept>
#include <opencv2/opencv.hpp>

namespace transkribus {

class Image
{
public:
	std::string url;
	cv::Mat mat;

	/// copy constructor
	Image(const Image& image);
	/// move constructor
	Image(Image&& image);

	/// inits image using a url
	Image(const std::string& url);

	/// inits image using cv::Mat reference
	Image(cv::Mat& mat);

	virtual ~Image() { }

	/// display the image using highgui
	void display() const;

	int getWidth() const { return mat.cols; }
	int getHeight() const { return mat.rows; }

	std::string& getUrl() { return url; }
	cv::Mat& getMat() { return mat; }

private:

};

std::ostream& operator<<(std::ostream& os, const Image& image);

} // end of namespace transkribus
