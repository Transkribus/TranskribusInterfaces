PROJECT(MyWriterRetrieval)

IF(EXISTS ${CMAKE_SOURCE_DIR}/../CMakeUser.cmake)
	include(${CMAKE_SOURCE_DIR}/../CMakeUser.cmake)
ENDIF()

# include macros needed
include("${CMAKE_CURRENT_SOURCE_DIR}/../Utils.cmake")

SET(PLUGIN_VERSION "0.0.1")
add_definitions(-DPLUGIN_VERSION="${PLUGIN_VERSION}")


file(GLOB TI_PLUGIN_SOURCES "MyWriterRetrieval.cpp")
file(GLOB TI_PLUGIN_HEADERS "MyWriterRetrieval.h")

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

add_library(${TI_PLUGIN_NAME} SHARED ${TI_PLUGIN_SOURCES} ${TI_PLUGIN_HEADERS})
target_link_libraries(${TI_PLUGIN_NAME} ${TI_INTERFACE_NAME} ${OpenCV_LIBS} ${RDF_LIBS} Qt5::Core)
add_dependencies(${TI_PLUGIN_NAME} ${TI_INTERFACE_NAME})

if(MSVC) # linux does not need this
    set_target_properties(${TI_PLUGIN_NAME} PROPERTIES COMPILE_FLAGS "-DTI_DLL_EXPORT")
endif()

if(NOT MSVC) # enable soname
    set_property(TARGET ${PROJECT_NAME} PROPERTY VERSION ${TI_VERSION_MAJOR}.${TI_VERSION_MINOR}.${TI_VERSION_PATCH})
    set_property(TARGET ${PROJECT_NAME} PROPERTY SOVERSION ${TI_VERSION_MAJOR})
endif()

message(STATUS "${TI_PLUGIN_NAME} added...")

##################################################### add testing exe ##########
SET(TI_EXAMPLE_NAME Test${TI_PLUGIN_NAME})
file(GLOB TI_EXAMPLE_SOURCES "testApp.cpp")

add_executable(${TI_EXAMPLE_NAME} WIN32  MACOSX_BUNDLE ${TI_EXAMPLE_SOURCES})
target_link_libraries(${TI_EXAMPLE_NAME} ${TI_INTERFACE_NAME} ${OpenCV_LIBS} ${CURL_LIBRARY})
set_target_properties(${TI_EXAMPLE_NAME} PROPERTIES LINK_FLAGS "/SUBSYSTEM:CONSOLE")

add_dependencies(${TI_EXAMPLE_NAME} ${TI_INTERFACE_NAME})

if (MSVC)
	TI_COPY_DLLS()
endif()

# get_cmake_property(_variableNames VARIABLES)
# foreach (_variableName ${_variableNames})
#    message(STATUS "${_variableName}=${${_variableName}}")
# endforeach()
