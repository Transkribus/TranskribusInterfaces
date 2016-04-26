
#pragma once

#include <string>
////#include <opencv2/opencv.hpp> // diem: this file includes all modules of opencv (which is bad for an interface)
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

	/// copy constructor
	Image(const Image& image);
	/// move constructor
	Image(Image&& image);

	/// inits image using a url
	Image(const std::string& url);

	// diem: empty contructor added - this is needed to use Image as a member in other classes
	/// inits image using cv::Mat reference
	Image(const cv::Mat& mat = cv::Mat());	

	virtual ~Image() { };

	bool empty() const;

	/// display the image using highgui
	void display() const;

	int getWidth() const { return mMat.cols; }
	int getHeight() const { return mMat.rows; }
	
	std::string toString() const;

	void setMat(const cv::Mat& mat);
	cv::Mat mat() const;

	void setUrl(const std::string& url);
	std::string url() const;

private:

	// diem: these things should be private
	// here is a usecase why:
	// imagine cv::Mat is changed (in e.g. 2 years) with something else
	// then we can create a 'wrapper' here for compatibility without
	// the need of changing all plugins
	std::string mUrl;
	cv::Mat mMat;

};

TiExport std::ostream& operator<<(std::ostream& os, const Image& image);

} // end of namespace transkribus
