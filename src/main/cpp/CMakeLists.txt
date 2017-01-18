
# check cmake requirements
cmake_minimum_required(VERSION 3.0.3)

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

# load paths from the user file if exists 
if(EXISTS ${CMAKE_SOURCE_DIR}/CMakeUser.cmake)
	include(${CMAKE_SOURCE_DIR}/CMakeUser.cmake)
endif()

set(BINARY_NAME ${CMAKE_PROJECT_NAME})

# Find includes in corresponding build directories (needed if Qt is required)
set(CMAKE_INCLUDE_CURRENT_DIR ON)
# Instruct CMake to run moc automatically when needed (Qt).
set(CMAKE_AUTOMOC ON)

option(WITH_HIGHGUI "WITH HIGHGUI" OFF)
option(WITH_CURL "WITH CURL" OFF)


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

include(${CMAKE_CURRENT_SOURCE_DIR}/cmake/Utils.cmake)

TI_FIND_OPENCV()
TI_FIND_QT()
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

# do not use TI_CREATE_TARGETS macro here because these targets link against this interface
#
# add interface
add_library(${PROJECT_NAME} SHARED ${TI_INTERFACE_SOURCES} ${TI_INTERFACE_HEADERS})
target_include_directories(${PROJECT_NAME} 	PRIVATE ${OpenCV_INCLUDE_DIRS})
target_link_libraries(${PROJECT_NAME} ${OpenCV_LIBS} ${CURL_LIBRARY})
# interface flags
set_target_properties(${PROJECT_NAME} PROPERTIES ARCHIVE_OUTPUT_DIRECTORY_DEBUG ${CMAKE_BINARY_DIR}/libs)
set_target_properties(${PROJECT_NAME} PROPERTIES ARCHIVE_OUTPUT_DIRECTORY_RELEASE ${CMAKE_BINARY_DIR}/libs)
set_target_properties(${PROJECT_NAME} PROPERTIES ARCHIVE_OUTPUT_DIRECTORY_RELWITHDEBINFO ${CMAKE_BINARY_DIR}/libs)
if (MSVC)
	set_target_properties(${PROJECT_NAME} PROPERTIES COMPILE_FLAGS "-DTI_DLL_EXPORT")
endif()
# make RelWithDebInfo link against release instead of debug opencv dlls
set_target_properties(${PROJECT_NAME} PROPERTIES MAP_IMPORTED_CONFIG_RELWITHDEBINFO RELEASE)
set_target_properties(${PROJECT_NAME} PROPERTIES MAP_IMPORTED_CONFIG_MINSIZEREL RELEASE)

# add test program
set(TI_TEST_NAME "Test${PROJECT_NAME}")
add_executable(${TI_TEST_NAME} WIN32  MACOSX_BUNDLE ${TI_TEST_SOURCES} ${TI_TEST_HEADERS})
target_include_directories(${TI_TEST_NAME} PRIVATE ${OpenCV_INCLUDE_DIRS})
target_link_libraries(${TI_TEST_NAME} ${TI_INTERFACE_NAME} ${OpenCV_LIBS} ${CURL_LIBRARY} ${PROJECT_NAME})
set_target_properties(${TI_TEST_NAME} PROPERTIES LINK_FLAGS "/SUBSYSTEM:CONSOLE")

add_dependencies(${TI_TEST_NAME} ${PROJECT_NAME})
# make RelWithDebInfo link against release instead of debug opencv dlls
set_target_properties(${TI_TEST_NAME} PROPERTIES MAP_IMPORTED_CONFIG_RELWITHDEBINFO RELEASE)
set_target_properties(${TI_TEST_NAME} PROPERTIES MAP_IMPORTED_CONFIG_MINSIZEREL RELEASE)


# add plugins
option(WITH_TEST_PLUGIN "WITH TEST PLUGIN" OFF)
option(WITH_WRITERRETRIEVAL "WITH WRITER RETRIEVAL" OFF)

if (WITH_TEST_PLUGIN)
	add_subdirectory(example_plugin)
endif()

if (WITH_WRITERRETRIEVAL)
	add_subdirectory(writerretrieval_plugin)
endif()

#debug for printing out all variables 
# get_cmake_property(_variableNames VARIABLES)
# foreach (_variableName ${_variableNames})
    # message(STATUS "${_variableName}=${${_variableName}}")
# endforeach()
