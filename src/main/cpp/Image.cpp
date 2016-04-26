#include "Image.h"
#include "ImageUtils.h"

#include <iostream>

#ifndef WITHOUT_HIGHGUI
#include <opencv2/highgui.hpp>
#endif

namespace transkribus {

Image::Image(const Image& image) : url(image.url) {
	mat = image.mat.clone();

	std::cout << "copied image" << std::endl;
}

Image::Image(Image&& image) :url(std::move(image.url)), mat(std::move(image.mat)) {
	std::cout << "moved image" << std::endl;
}

Image::Image(const std::string& url) : url(url) {
	mat = ImageUtils::loadCvMatFromUrl(url);
}

Image::Image(cv::Mat& mat) : mat(mat) {
}

// diem: I would suggest to remove this function completely 
// every group should have their (debug) visualization in place anyhow
void Image::display() const {

#ifndef WITHOUT_HIGHGUI
	// diem: this creates a (heavy) dependency to highgui which should not be included in an interface
	std::string title = url.empty() ? "Image" : url;

	cv::namedWindow( title, CV_WINDOW_AUTOSIZE );
	cv::imshow(title, mat); //display image

	cvWaitKey(0); // press any key to exit
	cv::destroyWindow(title);
#endif
}

std::ostream& operator<<(std::ostream& os, const Image& image) {
	return os << image.toString();
	//return os << "Image, w = " << image.getWidth() << " h = " << image.getHeight() << " url = " << image.url;
}

} // end of namespace transkribus

