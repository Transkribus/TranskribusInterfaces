
#pragma once

//#include <iostream>
#include <ostream>
#include <string>
#include <stdexcept>
//#include <opencv2/opencv.hpp> // diem: this file includes all modules of opencv (which is bad for an interface)
#include <opencv2/core.hpp>

//using namespace std;	// diem: using namespace is not good here - since all modules are using this namespace too
// see: http://stackoverflow.com/questions/1452721/why-is-using-namespace-std-in-c-considered-bad-practice

// define the export
#ifndef TiExport

#ifdef TI_DLL_EXPORT
#define TiExport __declspec(dllexport)
#else
#define TiExport	// remove
#endif
#endif

// diem: I would consider using a shorter name here
namespace transkribus {

class TiExport Image
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
	
	const std::string toString() const {
		return "Image, w = " + std::to_string(getWidth()) + " h = " + std::to_string(getHeight()) + " url = " + url;
	}

private:

};

TiExport std::ostream& operator<<(std::ostream& os, const Image& image);

} // end of namespace transkribus
