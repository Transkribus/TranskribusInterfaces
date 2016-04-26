#pragma once

#include "IModule.h"

namespace transkribus {

class IHtr : public IModule {

public:
	virtual ~IHtr() {}

	virtual void createModel(const std::string& pathToModels, const std::vector<std::string>& pars) = 0;

	virtual void process(const std::string& pathToModels, Image& image,
			const std::string& xmlInOut,
			const std::string& storageDir,
			const std::vector<std::string>& lineIds,
			const std::vector<std::string>& props) = 0;

};

}


