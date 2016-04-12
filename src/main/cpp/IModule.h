
#pragma once

#include <iostream>
#include "Image.h"

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
    virtual std::string usage() = 0;

    /**
     * unique tool name, describing the main domain
     *
     * @return
     */
    virtual std::string getToolName() = 0;

    /**
     * version, matching [0-9]+\.[0-9]+\.[0-9]+
     *
     * @return
     */
    virtual std::string getVersion() = 0;

    /**
     * your organization
     *
     * @return
     */
    virtual std::string getProvider() = 0;
};

}
