#include "Image.h"

#include <iostream>

using namespace std;

namespace transkribus {

Image::Image(const std::string& url) : url(url) {

}

Image::Image(cv::Mat& mat) : mat(mat) {

}

}
