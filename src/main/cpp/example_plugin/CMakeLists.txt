
PROJECT(MyLayoutAnalysis)

IF(EXISTS ${CMAKE_SOURCE_DIR}/../CMakeUser.cmake)
	include(${CMAKE_SOURCE_DIR}/../CMakeUser.cmake)
ENDIF()

SET(PLUGIN_VERSION "0.0.1")
add_definitions(-DPLUGIN_VERSION="${PLUGIN_VERSION}")

# TODO: add the interface here!	
include_directories (
)

file(GLOB TI_PLUGIN_SOURCES "MyLayoutAnalysis.cpp")
file(GLOB TI_PLUGIN_HEADERS "MyLayoutAnalysis.h")

set(TI_PLUGIN_NAME ${PROJECT_NAME})

set(LIBRARY_DIR ${CMAKE_CURRENT_BINARY_DIR}/libs ${CMAKE_CURRENT_BINARY_DIR}) #add libs directory to library dirs
link_directories(${LIBRARY_DIR})

TI_CREATE_TARGETS()
message(STATUS "${PROJECT_NAME} added...")
