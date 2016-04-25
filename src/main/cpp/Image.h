
#pragma once

//#include <iostream>
#include <ostream>
#include <string>
#include <stdexcept>
//#include <opencv2/opencv.hpp> // diem: this file includes all modules of opencv (which is bad for an interface)
#include <opencv2/core.hpp>

using namespace std;

// define the export
#ifndef TiExport

#ifdef TI_DLL_EXPORT
#define TiExport __declspec(dllexport)
#else
#define TiExport	// remove
#endif
#endif

namespace transkribus {

class TiExport Image
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

TiExport ostream& operator<<(ostream& os, const Image& image);

} // end of namespace transkribus
