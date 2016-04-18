#pragma once

#include "IModule.h"
#include "IBaseline2Polygon.h"

using namespace std;

namespace transkribus {

class ITrainHtr : public IModule {

public:
	virtual ~ITrainHtr() {};

	virtual void trainHtr(const string& pathToModelsIn, const string& pathToModelsOut, const vector<string>& props, const string& inputDir) = 0;
	virtual void createTrainData(const vector<string>& pageXmls, const string& outputDir, IBaseline2Polygon& mapper) = 0;

};

}


