
#include <iostream>
#include <string>
//#include <opencv2/opencv.hpp>
#include "../Image.h"

#ifdef _MSC_BUILD // define subsystem for MSVC
#pragma comment (linker, "/SUBSYSTEM:CONSOLE")
#endif

using namespace std;
using namespace cv;
using namespace transkribus;

Image testImage(string& url) {
	Image img(url); // test opening image with url
	Image img2 = img; // test copy constructor

	cout << img << endl; // test printing

	img.display(); // test display
	return img;
}

int main(int argc, char** argv)
{
//	string url = "http://www.austriatraveldirect.com/files/INNNORD01041.jp"; // return 404
//	string url = "http://www.austriatraveldirect.com/files/INNNORD01041.jpg"; // ok
//	string url = "https://dbis-thure.uibk.ac.at/f/Get?id=UNKRNHSATTZGUUMKZBSBNOUC"; // ok
	string url = "/tmp/test.jpg"; // ok
	//string url = "/tmp/asdfadsf.pg"; // not found

#ifdef WIN32
	url = "C:/temp/test.jpg"; // ok
#endif

	if (argc > 1) {
		url = argv[1];
	}
	cout << "opening url: " << url << endl;

	try {
		Image img = testImage(url);
		Image img2 = std::move(img);

		cout << "moved = " << img2 << endl; // test printing
		cout << "old = " << img << endl; // test printing

	} catch (exception& e) {
		cout << "an error occured: " << e.what() << endl;
		return 1;
	}

	return 0;
}
