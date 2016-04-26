#include "Image.h"
#include "ImageUtils.h"

#include <iostream>

#ifndef WITHOUT_HIGHGUI
#include <opencv2/highgui.hpp>
#endif

namespace transkribus {

Image::Image(const Image& image) : mUrl(image.url()) {
	mMat = image.mat().clone();
	std::cout << "copied image" << std::endl;
}

Image::Image(Image&& image) : mUrl(std::move(image.url())), mMat(std::move(image.mat())) {
	std::cout << "moved image" << std::endl;
}

Image::Image(const std::string& url) : mUrl(url) {
	mMat = ImageUtils::loadCvMatFromUrl(url);
}

Image::Image(const cv::Mat& mat) : mMat(mat) {
}

bool Image::empty() const
{
	return mMat.empty() && mUrl.empty();
}

// diem: I would suggest to remove this function completely 
// every group should have their (debug) visualization in place anyhow
void Image::display() const {

#ifndef WITHOUT_HIGHGUI
	// diem: this creates a (heavy) dependency to highgui which should not be included in an interface
	std::string title = mUrl.empty() ? "Image" : mUrl;

	cv::namedWindow( title, CV_WINDOW_AUTOSIZE );
	cv::imshow(title, mMat); //display image

	cvWaitKey(0); // press any key to exit
	cv::destroyWindow(title);
#endif
}

std::string Image::toString() const
{
	return "Image, w = " + std::to_string(getWidth()) + " h = " + std::to_string(getHeight()) + " url = " + mUrl;
}

void Image::setMat(const cv::Mat & mat)
{
	mMat = mat;
}

cv::Mat Image::mat() const
{
	return mMat;
}

void Image::setUrl(const std::string & url)
{
	mUrl = url;
}

std::string Image::url() const
{
	return mUrl;
}

std::ostream& operator<<(std::ostream& os, const Image& image) {
	return os << image.toString();
	//return os << "Image, w = " << image.getWidth() << " h = " << image.getHeight() << " url = " << image.url;
}

} // end of namespace transkribus

