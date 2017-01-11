
PROJECT(MyLayoutAnalysis)

set(TI_PLUGIN_NAME ${PROJECT_NAME})

SET(PLUGIN_VERSION "0.0.1")
add_definitions(-DPLUGIN_VERSION="${PLUGIN_VERSION}")

# if ReadFramework is needed by plugin
TI_FIND_RDF()

set(LIBRARY_DIR ${CMAKE_CURRENT_BINARY_DIR}/libs ${CMAKE_CURRENT_BINARY_DIR}) #add libs directory to library dirs
include_directories (
	${LIBRARY_DIR}
	${RDF_BUILD_DIRECTORY}
)

file(GLOB TI_PLUGIN_SOURCES "MyLayoutAnalysis.cpp")
file(GLOB TI_PLUGIN_HEADERS "MyLayoutAnalysis.h")

file(GLOB TI_TEST_SOURCES "testApp.cpp")

TI_CREATE_TARGETS()
message(STATUS "${PROJECT_NAME} added...")
