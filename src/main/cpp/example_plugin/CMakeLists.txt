
PROJECT(MyLayoutAnalysis)

IF(EXISTS ${CMAKE_SOURCE_DIR}/../CMakeUser.cmake)
	include(${CMAKE_SOURCE_DIR}/../CMakeUser.cmake)
ENDIF()

# include macros needed
include("${CMAKE_CURRENT_SOURCE_DIR}/../Utils.cmake")

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

add_library(${TI_PLUGIN_NAME} SHARED ${TI_PLUGIN_SOURCES} ${TI_PLUGIN_HEADERS})
target_link_libraries(${TI_PLUGIN_NAME} ${TI_INTERFACE_NAME} ${OpenCV_LIBS}) 

add_dependencies(${TI_PLUGIN_NAME} ${TI_INTERFACE_NAME})

if(MSVC) # linux does not need this
    set_target_properties(${TI_PLUGIN_NAME} PROPERTIES COMPILE_FLAGS "-DTI_DLL_EXPORT")
endif()

if(NOT MSVC) # enable soname
    set_property(TARGET ${PROJECT_NAME} PROPERTY VERSION ${TI_VERSION_MAJOR}.${TI_VERSION_MINOR}.${TI_VERSION_PATCH})
    set_property(TARGET ${PROJECT_NAME} PROPERTY SOVERSION ${TI_VERSION_MAJOR})
endif()

message(STATUS "${TI_PLUGIN_NAME} added...")

# add testing exe
SET(TI_EXAMPLE_NAME Test${TI_PLUGIN_NAME})
file(GLOB TI_EXAMPLE_SOURCES "testApp.cpp")

add_executable(${TI_EXAMPLE_NAME} WIN32  MACOSX_BUNDLE ${TI_EXAMPLE_SOURCES})
target_link_libraries(${TI_EXAMPLE_NAME} ${TI_INTERFACE_NAME} ${OpenCV_LIBS} ${CURL_LIBRARY}) 

add_dependencies(${TI_EXAMPLE_NAME} ${TI_INTERFACE_NAME})

if (MSVC)
	TI_COPY_DLLS()
endif()
