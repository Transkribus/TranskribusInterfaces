#include "Image.h"
#include "ImageUtils.h"

#include <iostream>

using namespace std;

namespace transkribus {

Image::Image(const Image& image) : url(image.url) {
	mat = image.mat.clone();

	cout << "copied image" << endl;
}

Image::Image(Image&& image) :url(std::move(image.url)), mat(std::move(image.mat)) {
	cout << "moved image" << endl;
}

Image::Image(const std::string& url) : url(url) {
	mat = ImageUtils::loadCvMatFromUrl(url);
}

Image::Image(cv::Mat& mat) : mat(mat) {
}

void Image::display() const {
	std::string title = url.empty() ? "Image" : url;

	cv::namedWindow( title, CV_WINDOW_AUTOSIZE );
	cv::imshow(title, mat); //display image

	cvWaitKey(0); // press any key to exit
	cv::destroyWindow("Image output");
}

ostream& operator<<(ostream& os, const Image& image) {
	return os << "Image, w = " << image.getWidth() << " h = " << image.getHeight() << " url = " << image.url;
}

} // end of namespace transkribus

