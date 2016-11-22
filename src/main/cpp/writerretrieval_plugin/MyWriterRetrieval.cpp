
#include "MyWriterRetrieval.h"
#include "../Image.h"

#include <iostream>

#include "WriterRetrieval.h"

namespace transkribus {


MyWriterRetrieval::MyWriterRetrieval() {}

MyWriterRetrieval::~MyWriterRetrieval()
{
	std::cout << "MyWriterRetrieval is deleted!" << std::endl;
}

std::string MyWriterRetrieval::process(const Image& image, const std::string& xmlIn, 
									   const std::vector<std::string>& ids,
										const std::vector<std::string>& props) {
	std::cout << "MyWriterRetrieval, process, xmlIn =  " << xmlIn << std::endl;
	std::cout << image << std::endl;

	return "";
}

Image MyWriterRetrieval::distances(const Image& features,
								   const std::vector<std::string>& ids,
								   const std::vector<std::string>& props) {
	return Image();
}

Image MyWriterRetrieval::train(const std::vector<Image>& features,
							   const std::vector<std::string>& ids,
							   const std::vector<std::string>& props) {
	return Image();
}


const std::string MyWriterRetrieval::usage() const {
	return std::string("MyUsage");
}

const std::string MyWriterRetrieval::getToolName() const {
	return std::string("Writer Retrieval");
}

const std::string MyWriterRetrieval::getVersion() const {
	return std::string("0.0.1");
}

const std::string MyWriterRetrieval::getProvider() const {
	return std::string("MyProvider");
}


IModule * MyWriterRetrievalModuleFactory::create(const std::vector<std::string>& pars)
{
	std::cout << "creating MyWriterRetrieval" << std::endl;
	return new MyWriterRetrieval;
}

}
