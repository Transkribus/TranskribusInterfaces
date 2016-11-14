#pragma once

#include "IModule.h"

//using namespace std;

namespace transkribus {

class ITrainHtr : public IModule {

public:
	virtual ~ITrainHtr() {};

	virtual void trainHtr(
		const std::string& pathToModelsIn, 
		const std::string& pathToModelsOut, 
		const std::string& inputTrainDir,
		const std::string& inputValDir, 
		const std::vector<std::string>& props) = 0;
	
	virtual void createTrainData(
		const std::vector<std::string>& pageXmls, 
		const std::string& outputDir, 
		const std::vector<std::string>& props) = 0;
	
	virtual void createHtr(
		const std::string& pathToModelsOut, 
		const std::string& pathToCharMapFile, 
		const std::vector<std::string>& props) = 0;
};

}


