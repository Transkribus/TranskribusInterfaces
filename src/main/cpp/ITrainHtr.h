#pragma once

#include "IModule.h"

namespace transkribus {

class ITrainHtr : public IModule {

public:
	virtual ~ITrainHtr() {};

	virtual void trainHtr(const std::string& pathToModelsIn, const std::string& pathToModelsOut, const std::vector<std::string>& props, const std::string& inputDir) = 0;
	virtual void createTrainData(const std::vector<std::string>& pageXmls, const std::string& outputDir, const std::vector<std::string>& props) = 0;

};

}


