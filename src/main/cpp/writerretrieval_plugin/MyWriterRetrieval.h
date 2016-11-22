
#pragma once

#include "../IWriterRetrieval.h"		// diem: the include directory should be added to the project rather than using relative includes
#include "../ModuleFactory.h"

#pragma warning(disable: 4251)	// dll interface

namespace transkribus {

// 1st step: Create and implementation of your module, inherited from the corresponding interface class
class MyWriterRetrieval : public IWriterRetrieval
{
public:

	MyWriterRetrieval();

	virtual ~MyWriterRetrieval();

	std::string process(
		const Image& image,
		const std::string& xmlIn,
		const std::vector<std::string>& ids = std::vector<std::string>(),
		const std::vector<std::string>& props = std::vector<std::string>());

	Image distances(
		const Image& features,
		const std::vector<std::string>& ids = std::vector<std::string>(),
		const std::vector<std::string>& props = std::vector<std::string>());

	Image train(
		const std::vector<Image>& features,
		const std::vector<std::string>& ids,
		const std::vector<std::string>& props);

    const std::string usage() const;

    const std::string getToolName() const;

    const std::string getVersion() const;

    const std::string getProvider() const;
};

// 2nd step: for every module, you have to implement a ModuleFactory, which creates an instance of your module with some parameters
class TiExport MyWriterRetrievalModuleFactory : public ModuleFactory
{
public:
	IModule* create(const std::vector<std::string>& pars);	// diem: implementations should be in the cpp

};

}

// 3rd step, IMPORTANT: you have to declare an instance of your factory with the name 'ModuleFactoryInstance' outside of any namespace!
// This enables us to load the shared lib with dlopen and access the factory instance without and mangled name issues
extern "C" {
	TiExport transkribus::MyWriterRetrievalModuleFactory ModuleFactoryInstance;
}
