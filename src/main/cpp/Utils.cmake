macro(TI_CREATE_TARGETS)

# create the targets
set(TI_TEST_NAME 		${PROJECT_NAME}Test)
set(TI_EXAMPLE_NAME 	${PROJECT_NAME}Example)
set(TI_INTERFACE_NAME 	${PROJECT_NAME})

# set(LIB_TEST_NAME 		optimized ${DLL_TEST_NAME}.lib 		debug ${DLL_TEST_NAME}d.lib)
# set(LIB_EXAMPLE_NAME 	optimized ${DLL_EXAMPLE_NAME}.lib 	debug ${DLL_EXAMPLE_NAME}d.lib)
set(LIB_INTERFACE_NAME	optimized ${TI_INTERFACE_NAME}.lib debug ${TI_INTERFACE_NAME}d.lib)

set(LIBRARY_DIR ${CMAKE_CURRENT_BINARY_DIR}/libs ${CMAKE_CURRENT_BINARY_DIR}) #add libs directory to library dirs
link_directories(${LIBRARY_DIR})

set_target_properties(${OpenCV_LIBS} PROPERTIES MAP_IMPORTED_CONFIG_RELWITHDEBINFO RELEASE)

# add interface
add_library(${TI_INTERFACE_NAME} SHARED ${TI_INTERFACE_SOURCES} ${TI_INTERFACE_HEADERS})
target_link_libraries(${TI_INTERFACE_NAME} ${OpenCV_LIBS})
 
# interface flags
set_target_properties(${TI_INTERFACE_NAME} PROPERTIES ARCHIVE_OUTPUT_DIRECTORY_DEBUG ${CMAKE_BINARY_DIR}/libs)
set_target_properties(${TI_INTERFACE_NAME} PROPERTIES ARCHIVE_OUTPUT_DIRECTORY_RELEASE ${CMAKE_BINARY_DIR}/libs)
set_target_properties(${TI_INTERFACE_NAME} PROPERTIES ARCHIVE_OUTPUT_DIRECTORY_RELWITHDEBINFO ${CMAKE_BINARY_DIR}/libs)

set_target_properties(${TI_INTERFACE_NAME} PROPERTIES COMPILE_FLAGS "-DTI_DLL_EXPORT")
set_target_properties(${TI_INTERFACE_NAME} PROPERTIES DEBUG_OUTPUT_NAME ${TI_INTERFACE_NAME}d)
set_target_properties(${TI_INTERFACE_NAME} PROPERTIES RELEASE_OUTPUT_NAME ${TI_INTERFACE_NAME})

set(TI_INTERFACE_LIB_PATH ${CMAKE_CURRENT_BINARY_DIR}/libs/${TI_INTERFACE_NAME})
 
set_property(TARGET ${TI_INTERFACE_NAME} PROPERTY VERSION ${TI_VERSION_MAJOR}.${NOMACS_VERSION_MINOR}.${NOMACS_VERSION_PATCH})
set_property(TARGET ${TI_INTERFACE_NAME} PROPERTY SOVERSION ${TI_VERSION_MAJOR})

# add test
add_executable(${TI_TEST_NAME} WIN32  MACOSX_BUNDLE ${TI_TEST_SOURCES} ${TI_TEST_HEADERS})
target_link_libraries(${TI_TEST_NAME} ${TI_INTERFACE_NAME} ${OpenCV_LIBS}) 

# add example plugin
add_executable(${TI_EXAMPLE_NAME} WIN32  MACOSX_BUNDLE ${TI_EXAMPLE_SOURCES} ${TI_EXAMPLE_HEADERS})
target_link_libraries(${TI_EXAMPLE_NAME} ${TI_INTERFACE_NAME} ${OpenCV_LIBS}) 

add_dependencies(${TI_TEST_NAME} ${TI_INTERFACE_NAME})
add_dependencies(${TI_EXAMPLE_NAME} ${TI_INTERFACE_NAME})

target_include_directories(${TI_INTERFACE_NAME} PRIVATE ${OpenCV_INCLUDE_DIRS})
target_include_directories(${TI_TEST_NAME} 		PRIVATE ${OpenCV_INCLUDE_DIRS})
target_include_directories(${TI_EXAMPLE_NAME} 	PRIVATE ${OpenCV_INCLUDE_DIRS})

endmacro(TI_CREATE_TARGETS)

macro(TI_FIND_OPENCV)

unset(OpenCV_CONFIG_PATH CACHE)

# OpenCV
IF (WITH_HIGHGUI)
	SET(OpenCV_REQUIRED_MODULES core highgui)
ELSE()
	SET(OpenCV_REQUIRED_MODULES core highgui)
ENDIF()
SET(OpenCV_LIBS "")

IF (OpenCV_LIBS STREQUAL "") 
	find_package(OpenCV)
ENDIF()

IF (NOT OpenCV_FOUND)
	message(FATAL_ERROR "OpenCV not found. Please specify a valid path") 
ELSE()
	unset(OPENCV_LIBS)

	IF (WITH_HIGHGUI)	
		add_definitions(-DWITH_OPENCV)
		set(OpenCV_LIBS opencv_core opencv_highgui)
	ELSE()
		add_definitions(-DWITH_OPENCV -DWITHOUT_HIGHGUI)
		set(OpenCV_LIBS opencv_core)
	ENDIF()

	message(STATUS "OpenCV Modules ${OpenCV_LIBS} added...")
ENDIF()
# OpenCV end

endmacro(TI_FIND_OPENCV)

macro(TI_COPY_DLLS)

IF (WITH_HIGHGUI)
	set(ti_all_cv_libs ${OpenCV_LIBS} opencv_imgproc310 opencv_imgcodecs310 opencv_videoio310)
ELSE ()
	set(ti_all_cv_libs ${OpenCV_LIBS})
ENDIF()

# copy required dlls to the directories
foreach(opencvlib ${ti_all_cv_libs})
	file(GLOB dllpath ${OpenCV_DIR}/bin/Release/${opencvlib}*.dll)
	file(COPY ${dllpath} DESTINATION ${CMAKE_CURRENT_BINARY_DIR}/Release)
	file(COPY ${dllpath} DESTINATION ${CMAKE_CURRENT_BINARY_DIR}/RelWithDebInfo)

	file(GLOB dllpath ${OpenCV_DIR}/bin/Debug/${opencvlib}*d.dll)
	file(COPY ${dllpath} DESTINATION ${CMAKE_CURRENT_BINARY_DIR}/Debug)
	message(STATUS "${dllpath} copied...")
endforeach(opencvlib)

endmacro(TI_COPY_DLLS)