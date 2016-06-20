#pragma once

#include "IModule.h"

using namespace std;

namespace transkribus {

class ITrainHtr : public IModule {

public:
	virtual ~ITrainHtr() {};

	virtual void trainHtr(const string& pathToModelsIn, const string& pathToModelsOut, const string& inputTrainDir,
		const string& inputValDir, const vector<string>& props) = 0;
	virtual void createTrainData(const vector<string>& pageXmls, const string& outputDir, const vector<string>& props) = 0;
	virtual void createHtr(const string& pathToModelsOut, const string& pathToCharMapFile, const vector<string>& props) = 0;
};

}


