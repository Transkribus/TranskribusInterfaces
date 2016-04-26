
#include <iostream>
#include <string>
//#include <opencv2/opencv.hpp>
#include "../Image.h"

#ifdef _MSC_BUILD // define subsystem for MSVC
#pragma comment (linker, "/SUBSYSTEM:CONSOLE")
#endif

transkribus::Image testImage(std::string& url) {
	transkribus::Image img(url); // test opening image with url
	transkribus::Image img2 = img; // test copy constructor

	std::cout << img << std::endl; // test printing

	img.display(); // test display
	return img;
}

int main(int argc, char** argv)
{

	std::string url = "/tmp/test.jpg"; // ok
	//string url = "/tmp/asdfadsf.pg"; // not found

#ifdef WIN32
	url = "C:/temp/test.jpg"; // ok
#endif

	//url = "http://www.austriatraveldirect.com/files/INNNORD01041.jp"; // return 404
	//url = "http://www.austriatraveldirect.com/files/INNNORD01041.jpg"; // ok
	//url = "https://dbis-thure.uibk.ac.at/f/Get?id=UNKRNHSATTZGUUMKZBSBNOUC"; // ok

	if (argc > 1) {
		url = argv[1];
	}
	std::cout << "opening url: " << url << std::endl;

	try {
		transkribus::Image img = testImage(url);
		transkribus::Image img2 = std::move(img);

		std::cout << "moved = " << img2 << std::endl; // test printing
		std::cout << "old = " << img << std::endl; // test printing

	} catch (std::exception& e) {
		std::cout << "an error occured: " << e.what() << std::endl;
		return 1;
	}

	return 0;
}
