#pragma once

#include <string>
#include "IModule.h"

using namespace std;

namespace transkribus {

class IHtr : public IModule {

public:
	virtual ~IHtr() {}

	virtual void createModel(const string& pathToModels, const vector<string>& pars) = 0;

	virtual void process(const string& pathToModels, Image& image,
			const string& xmlInOut,
			const string& storageDir,
			const vector<string>& lineIds,
			const vector<string>& props) = 0;

};

}


