#include <iostream>
#include <stdexcept>
#include "IWriterRetrieval.h"
#include "ModuleFactory.h"
#include "Image.h"
#include "Utils.h"

#include <QCoreApplication>

#ifndef WIN32
#include <dlfcn.h>
#endif

#ifdef _MSC_BUILD // define subsystem for MSVC
#pragma comment (linker, "/SUBSYSTEM:CONSOLE")
#endif

// declare local functions
void* loadLibrary(std::string& libName);
void test();
cv::Mat vecOfCSVStringToMat(std::vector<std::string> test);

int main(int argc, char** argv) {
	
	rdf::Utils::initDefaultFramework();
	QCoreApplication app(argc, (char**)argv);

	test();
	test();

	//std::string testString  = "-0.057322,0.0148466,-0.0701324";
	//std::vector<std::string> testVec;
	//testVec.push_back(testString);
	//cv::Mat featMat = vecOfCSVStringToMat(testVec);
	//std::cout << featMat;

	return 0;
}

#ifndef WIN32
void* loadLibrary(std::string& libName) {
	void* library_handle = dlopen(libName.c_str(), RTLD_NOW | RTLD_GLOBAL);

	if (!library_handle) {
		throw std::runtime_error("cannot load library "+libName+" error: "+dlerror());
	}

	return library_handle;
}

void test() {
	transkribus::ILayoutAnalysis* la;

	std::string libName("./libMyLayoutAnalysis.so");
	std::cout << "opening lib: " << libName << std::endl;

	transkribus::Image image("/tmp/test.jpg");

	std::vector<std::string> constructorPars;
	transkribus::IModule* module = transkribus::ModuleFactory::createModuleFromLib(libName, constructorPars);
	la = transkribus::ModuleFactory::castILayoutAnalysis(module);

	std::vector<std::string> ids;
	std::vector<std::string> props;
	la->process(image, "pageXmlFileUrl", ids, props);

}

#else
cv::Mat vecOfCSVStringToMat(std::vector<std::string> strings) {
	if(strings.size() == 0)
		return cv::Mat();

	std::vector<std::vector<float> > feat;
	for(int i = 0; i < strings.size(); i++) {
		feat.push_back(std::vector<float>());
		std::stringstream sstream(strings[i]);
		while(sstream.good()) {
			std::string substr;
			getline(sstream, substr, ',');
			feat[i].push_back((float)atof(substr.c_str()));
		}
	}

	if(feat.size() == 0)
		return cv::Mat();

	cv::Mat res = cv::Mat((int)strings.size(), (int)feat[0].size(), CV_32F);
	for(int i = 0; i < feat.size(); i++) {
		for(int j = 0; j < feat[i].size(); j++)
			res.at<float>(i, j) = feat[i][j];
	}
	return res;
}

void test() {
	transkribus::IWriterRetrieval* wr;
#ifdef _DEBUG
	std::string libName("MyWriterRetrievald.dll");
#else
	std::string libName("MyWriterRetrieval.dll");
#endif

	//transkribus::Image image("C:/temp/test.jpg");
	//transkribus::Image image("D:/Databases/icdar2011/cropped/1-1.png");
	transkribus::Image image("D:/ABP_FirstTestCollection/M_Aigen_am_Inn_007_0021.jpg");
	std::string xmlFile = "D:/ABP_FirstTestCollection/page/M_Aigen_am_Inn_007_0021.xml";

	transkribus::Image image2("D:/Databases/icdar2011/cropped/1-2.png");
	transkribus::Image image3("D:/Databases/icdar2011/cropped/2-1.png");

	std::vector<std::string> constructorPars;
	try {
		transkribus::IModule* module = transkribus::ModuleFactory::createModuleFromLib(libName, constructorPars);
		wr = transkribus::ModuleFactory::castIWriterRetrieval(module); 

		std::vector<std::string> ids;
		std::vector<std::string> props;
		//std::string feature = wr->process(image, "pageXmlFileUrl", ids, props);
		std::string feature = wr->process(image, xmlFile, ids, props);

		std::cout << "writer retrieval feature:" << std::endl;
		std::cout << feature << std::endl;


		std::string feature2 = wr->process(image2, "pageXmlFileUrl", ids, props);
		std::string feature3 = wr->process(image3, "pageXmlFileUrl", ids, props);

		std::vector<std::string> strings;
		strings.push_back(feature);
		strings.push_back(feature2);
		strings.push_back(feature3);
		cv::Mat featMat = vecOfCSVStringToMat(strings);
		transkribus::Image i = transkribus::Image(featMat);

		transkribus::Image dist = wr->distances(i, ids, props);
		std::cout << "distances:" << std::endl;
		std::cout << dist.mat() << std::endl;

		delete module;	// diem: the module has to be deleted after use!
	}
	catch (std::exception e) {
		std::cout << "test failed: " << e.what() << std::endl;
	}
	catch(...) {
		std::cout << "unknown error: test failed..." << std::endl;
		return;
	}
	
}
#endif
