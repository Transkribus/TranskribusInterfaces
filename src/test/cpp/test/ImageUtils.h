
//#include <stdio.h>
//#include <curl/curl.h>
#include <sstream>
#include <iostream>
//#include <stdexcept>
//#include <vector>
#include <opencv2/opencv.hpp>

namespace transkribus {

class ImageUtils {
public:
	static cv::Mat loadCvMatFromUrl(const std::string& url);

	static size_t write_data(char *ptr, size_t size, size_t nmemb, void *userdata);
};

}
