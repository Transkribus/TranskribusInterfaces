
#pragma once

#include <string>

namespace transkribus {

class IModule
{
public:
	virtual ~IModule() {}

    /**
     * returns a String (\n) for linebreak to describe the module and its
     * behavior
     *
     * @return
     */
    virtual const std::string usage() const = 0;

    /**
     * unique tool name, describing the main domain
     *
     * @return
     */
    virtual const std::string getToolName() const = 0;

    /**
     * version, matching [0-9]+\.[0-9]+\.[0-9]+
     *
     * @return
     */
    virtual const std::string getVersion() const = 0;

    /**
     * your organization
     *
     * @return
     */
    virtual const std::string getProvider() const = 0;
};

}
