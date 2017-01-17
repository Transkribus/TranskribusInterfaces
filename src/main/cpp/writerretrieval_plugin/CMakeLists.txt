PROJECT(MyWriterRetrieval)

SET(PLUGIN_VERSION "0.0.1")
add_definitions(-DPLUGIN_VERSION="${PLUGIN_VERSION}")



set(TI_PLUGIN_NAME ${PROJECT_NAME})

# ReadFramework is needed by Writer Retrieval plugin
TI_FIND_RDF()

set(LIBRARY_DIR ${CMAKE_CURRENT_BINARY_DIR}/libs ${CMAKE_CURRENT_BINARY_DIR}) #add libs directory to library dirs
link_directories(
	${LIBRARY_DIR}
	${RDF_BUILD_DIRECTORY}
)

include_directories (
	${RDF_INCLUDE_DIRECTORY}
)

file(GLOB TI_PLUGIN_SOURCES "MyWriterRetrieval.cpp")
file(GLOB TI_PLUGIN_HEADERS "MyWriterRetrieval.h")

file(GLOB TI_TEST_SOURCES "testApp.cpp")


TI_CREATE_TARGETS()
message(STATUS "${TI_PLUGIN_NAME} added...")
