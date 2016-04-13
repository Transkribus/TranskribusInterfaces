/*
http://benjithian.sg/2012/12/load-image-from-memoryweb-using-opencv-and-libcurl/
I just started playing around with OpenCV and wanted it to display images directly from memory loaded from  a website URL.
Below is the C++ codes to load an image directly from a website without it being written to the hard disk.
Library used were OpenCV and Libcurl. Do tell me if there is a more efficient way to do it.
*/
#include <stdio.h>
#include <curl/curl.h>
#include <sstream>
#include <iostream>
#include <stdexcept>
#include <vector>
#include <opencv2/opencv.hpp>

using namespace std;

//curl writefunction to be passed as a parameter
size_t write_data(char *ptr, size_t size, size_t nmemb, void *userdata) {
    std::ostringstream *stream = (std::ostringstream*)userdata;
    size_t count = size * nmemb;
    stream->write(ptr, count);
    return count;
}

//function to retrieve the image as Cv::Mat data type
cv::Mat loadCvMatFromUrl(const std::string& url)
{
	CURL *curl;
	CURLcode res;
	std::ostringstream stream;
	curl = curl_easy_init();
	if (!curl) {
		throw std::runtime_error("could not initialize curl");
	}
    curl_easy_setopt(curl, CURLOPT_URL, url.c_str()); //the img url

#if 0
    cout << "using ssl" << endl;
    curl_easy_setopt(curl, CURLOPT_USE_SSL, CURLUSESSL_ALL);
#endif

#if 1
    cout << "following redirect location" << endl;
    curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);
#endif

	curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_data); // pass the writefunction
    curl_easy_setopt(curl, CURLOPT_WRITEDATA, &stream); // pass the stream ptr when the writefunction is called
	res = curl_easy_perform(curl); // start curl

	cout << "curl res code = " << res << endl;
	if (res != 0) {
		throw std::runtime_error("Could not load image from "+url+" - curl error: "+curl_easy_strerror(res));
	}

	std::string output = stream.str(); // convert the stream into a string
	curl_easy_cleanup(curl); // cleanup
	std::vector<char> data = std::vector<char>( output.begin(), output.end() ); //convert string into a vector

	cv::Mat data_mat = cv::Mat(data); // create the cv::Mat datatype from the vector
	cv::Mat image = cv::imdecode(data_mat,1); //read an image from memory buffer

	cout << "read image w = " << image.cols << " h = " << image.rows << endl;

	return image;
}

int main(int argc, char** argv)
{
//	string url = "http://www.austriatraveldirect.com/files/INNNORD01041.jpg";
	string url = "http://dbis-thure.uibk.ac.at/f/Get?id=UNKRNHSATTZGUUMKZBSBNOUC";
	if (argc > 1) {
		url = argv[1];
	}
	cout << "opening url: " << url << endl;

	try {
		cv::Mat image = loadCvMatFromUrl(url);
		cv::namedWindow( "Image output", CV_WINDOW_AUTOSIZE );

		cv::imshow("Image output",image); //display image
		cvWaitKey(0); // press any key to exit
		cv::destroyWindow("Image output");
	} catch (exception& e) {
		cout << "An error occured: " << e.what() << endl;
		return 1;
	}


}
