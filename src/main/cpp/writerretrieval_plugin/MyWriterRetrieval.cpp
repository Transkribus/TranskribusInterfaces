
#include "MyWriterRetrieval.h"
#include "../Image.h"

#include <iostream>

#include "WriterRetrieval.h"
#include "WriterDatabase.h"

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

	QSharedPointer<rdf::WriterRetrievalConfig> wrc = QSharedPointer<rdf::WriterRetrievalConfig>(new rdf::WriterRetrievalConfig());
	wrc->loadSettings();

	rdf::WriterRetrieval wr = rdf::WriterRetrieval(image.mat());
	wr.setConfig(wrc);
	wr.setXmlPath(xmlIn);
	wr.compute();

	cv::Mat i = wr.draw(image.mat());
	Image i2 = Image(i);
	i2.display();

	cv::Mat feature = wr.getFeature();
	
	QString csvFeature;
	for(int i = 0; i < feature.rows; i++) {
		const float* r = feature.ptr<float>(i);
		for(int j = 0; j < feature.cols; j++) {
			csvFeature += QString::number(r[j]);
			if (j!=feature.cols-1)
				csvFeature += ",";

		}
	}
	return csvFeature.toStdString();
}

Image MyWriterRetrieval::distances(const Image& features,
								   const std::vector<std::string>& ids,
								   const std::vector<std::string>& props) {

	QSharedPointer<rdf::WriterRetrievalConfig> wrc = QSharedPointer<rdf::WriterRetrievalConfig>(new rdf::WriterRetrievalConfig());
	wrc->loadSettings();

	rdf::WriterVocabulary voc = wrc->vocabulary();

	return voc.calcualteDistanceMatrix(features.mat());
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
	return std::string("WriterRetrieval");
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
