
//#include <opencv2/opencv.hpp> // diem: do not include all opencv modules
#include <string>
#include <vector>

// declare cv::Mat
namespace cv {
	class Mat;
}

namespace transkribus {

class ImageUtils {
public:
	/// Returns a new cv::Mat object with an image loaded from the given url
	static cv::Mat loadCvMatFromUrl(const std::string& url);
	static cv::Mat loadCvMatFromUrl(const std::string& url, cv::Mat (*readingFunction)(const std::string& url));

	/// Reads the data from a given url into a char vector
	static std::vector<char> readFromUrl(const std::string& url, int expectedReturnCode=200, bool useSSL=false, bool followRedirect=true);

private:

	/// write_data function for curl CURLOPT_WRITEFUNCTION
	static size_t write_data(char *ptr, size_t size, size_t nmemb, void *userdata);
	static bool file_exists(const std::string& url);
	static cv::Mat cvRead(const std::string& url);
};

}
