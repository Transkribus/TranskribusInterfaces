#include "ImageUtils.h"

#include <stdio.h>
#include <sstream>
#include <iostream>
#include <fstream>
#include <stdexcept>
#include <vector>
//#include <opencv2/opencv.hpp> // diem: do not include all opencv modules

#include <opencv2/core.hpp>

#ifndef WITHOUT_HIGHGUI
#include <opencv2/highgui.hpp>
#endif

#ifndef WITHOUT_CURL
#include <curl/curl.h>
#endif

namespace transkribus {

bool ImageUtils::file_exists(const std::string& url) {
	std::ifstream my_file(url);
	return my_file.good();
}

size_t ImageUtils::write_data(char *ptr, size_t size, size_t nmemb, void *userdata) {
    std::ostringstream *stream = (std::ostringstream*)userdata;
    size_t count = size * nmemb;
    stream->write(ptr, count);
    return count;
}

#ifndef WITHOUT_CURL
std::vector<char> ImageUtils::readFromUrl(const std::string& url, int expectedReturnCode, bool useSSL, bool followRedirect) {
	CURL *curl;
	CURLcode curl_code;
	std::ostringstream stream;
	curl = curl_easy_init();
	if (!curl) {
		throw std::runtime_error("could not initialize curl");
	}
    curl_easy_setopt(curl, CURLOPT_URL, url.c_str()); //the img url

	if (useSSL) {
		std::cout << "using ssl" << std::endl;
		curl_easy_setopt(curl, CURLOPT_USE_SSL, CURLUSESSL_ALL);
	}

	if (followRedirect) {
		std::cout << "following redirect location" << std::endl;
		curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);
	}

	curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, ImageUtils::write_data); // pass the writefunction
    curl_easy_setopt(curl, CURLOPT_WRITEDATA, &stream); // pass the stream ptr when the writefunction is called
    curl_code = curl_easy_perform(curl); // start curl

	long http_code = 0;
	curl_easy_getinfo(curl, CURLINFO_RESPONSE_CODE, &http_code);
	if (expectedReturnCode!=-1 && http_code != expectedReturnCode) {
		throw std::runtime_error("Could not load image from "+url+" - http response code was: "+std::to_string(http_code));
	}

	std::cout << "curl res code = " << curl_code << std::endl;
	if (curl_code != 0) {
		throw std::runtime_error("Could not load image from "+url+" - curl error: "+curl_easy_strerror(curl_code));
	}

	std::string output = stream.str(); // convert the stream into a string
	curl_easy_cleanup(curl); // cleanup
	std::vector<char> data = std::vector<char>( output.begin(), output.end() ); //convert string into a vector

	return data;
}
#else
std::vector<char> ImageUtils::readFromUrl(const std::string& url, int expectedReturnCode, bool useSSL, bool followRedirect) { return std::vector<char>(); }
#endif 

cv::Mat ImageUtils::loadCvMatFromUrl(const std::string& url) 
{

	return loadCvMatFromUrl(url, &cvRead);
}


cv::Mat ImageUtils::cvRead(const std::string& url) {

#ifndef WITHOUT_HIGHGUI
	return cv::imread(url);
#else
	std::cerr << "cannot read " << url << "since cv::imread is called, but highgui is not available" << std::endl;
	return cv::Mat();
#endif
}

cv::Mat ImageUtils::loadCvMatFromUrl(const std::string& url, cv::Mat (*readingFunction)(const std::string& url))
{

	if (ImageUtils::file_exists(url)) {
		cv::Mat image = readingFunction(url);
		return image;
	}
	else {

#ifndef WITHOUT_HIGHGUI
		std::vector<char> data = readFromUrl(url);
		std::cout << "read data, size = " << data.size() << std::endl;

		cv::Mat data_mat = cv::Mat(data); // create the cv::Mat datatype from the vector
		cv::Mat image = cv::imdecode(data_mat, 1); //read an image from memory buffer

		std::cout << "read image w = " << image.cols << " h = " << image.rows << std::endl;
		return image;
#else
		std::cerr << "cannot read " << url << "since cv::imdecode is called, but highgui is not available" << std::endl;
#endif // WITHOUT_HIGHGUI
	}

	return cv::Mat();
}

} // end of namespace transkribus
