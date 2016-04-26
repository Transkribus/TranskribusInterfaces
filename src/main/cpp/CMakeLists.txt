
# check cmake requirements
cmake_minimum_required(VERSION 2.8)

######################## begin of project
project(TranskribusInterfaces)

if(CMAKE_CL_64)
	SET(TI_ARCHITECTURE "x64")
else()
	SET(TI_ARCHITECTURE "x86")
endif()

set(TI_VERSION_MAJOR 0)
set(TI_VERSION_MINOR 1)
set(TI_VERSION_PATCH 0)
set(TI_VERSION ${TI_VERSION_MAJOR}.${TI_VERSION_MINOR})

add_definitions(-DTI_VERSION="${TI_VERSION}")

set(BINARY_NAME ${CMAKE_PROJECT_NAME})

option(WITH_HIGHGUI "WITH HIGHGUI" OFF)
option(WITH_CURL "WITH CURL" OFF)

# load paths from the user file if exists 
if(EXISTS ${CMAKE_SOURCE_DIR}/CMakeUser.cmake)
	include(${CMAKE_SOURCE_DIR}/CMakeUser.cmake)
endif()

# check compiler
include(CheckCXXCompilerFlag)
CHECK_CXX_COMPILER_FLAG("-std=c++11" COMPILER_SUPPORTS_CXX11)
CHECK_CXX_COMPILER_FLAG("-std=c++0x" COMPILER_SUPPORTS_CXX0X)

if(COMPILER_SUPPORTS_CXX11)
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
elseif(COMPILER_SUPPORTS_CXX0X)
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++0x")
elseif(NOT MSVC)
	message(STATUS "The compiler ${CMAKE_CXX_COMPILER} has no C++11 support. Please use a different C++ compiler.")
endif()

# set output path (also for all subdirectories), so that we do not have to copy the libraries afterwards
if(MSVC)
	set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/$<CONFIGURATION>)
	set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/$<CONFIGURATION>)
	set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/$<CONFIGURATION>)
else()
	set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/)
	set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/)
	set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/)	
endif()

include(${CMAKE_CURRENT_SOURCE_DIR}/Utils.cmake)

TI_FIND_OPENCV()
TI_FIND_CURL()

file(GLOB TI_TEST_SOURCES "test_image/*.cpp")
file(GLOB TI_TEST_HEADERS "test_image/*.h")

# interface
file(GLOB TI_INTERFACE_SOURCES "*.cpp")
file(GLOB TI_INTERFACE_HEADERS "*.h")

# gather information for building
include_directories (
	${CMAKE_CURRENT_SOURCE_DIR}
	${CMAKE_CURRENT_SOURCE_DIR}/test_image
)

TI_CREATE_TARGETS()

# add test plugin
option(WITH_TEST_PLUGIN "WITH TEST PLUGIN" OFF)

if (WITH_TEST_PLUGIN)
	add_subdirectory(example_plugin)
endif()

if (MSVC)
	TI_COPY_DLLS()
endif()

#debug for printing out all variables 
# get_cmake_property(_variableNames VARIABLES)
# foreach (_variableName ${_variableNames})
#     message(STATUS "${_variableName}=${${_variableName}}")
# endforeach()
