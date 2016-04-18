
#pragma once

//#include <iostream>
#include <ostream>
#include <string>
#include <stdexcept>
#include <opencv2/opencv.hpp>

using namespace std;

namespace transkribus {

class Image
{
public:
	string url;
	cv::Mat mat;

	/// copy constructor
	Image(const Image& image);
	/// move constructor
	Image(Image&& image);

	/// inits image using a url
	Image(const string& url);

	/// inits image using cv::Mat reference
	Image(cv::Mat& mat);

	virtual ~Image() { }

	/// display the image using highgui
	void display() const;

	int getWidth() const { return mat.cols; }
	int getHeight() const { return mat.rows; }
	
	const string toString() const {
		return "Image, w = " + to_string(getWidth()) + " h = " + to_string(getHeight()) + " url = " + url;
	}

private:

};

ostream& operator<<(ostream& os, const Image& image);

} // end of namespace transkribus
