macro(TI_CREATE_TARGETS)

# create the targets
set(TI_TEST_NAME 		${PROJECT_NAME}Test)
set(TI_EXAMPLE_NAME 	${PROJECT_NAME}Example)
set(TI_INTERFACE_NAME 	${PROJECT_NAME})

# set(LIB_TEST_NAME 		optimized ${DLL_TEST_NAME}.lib 		debug ${DLL_TEST_NAME}d.lib)
# set(LIB_EXAMPLE_NAME 	optimized ${DLL_EXAMPLE_NAME}.lib 	debug ${DLL_EXAMPLE_NAME}d.lib)
set(LIB_INTERFACE_NAME	optimized ${TI_INTERFACE_NAME}.lib debug ${TI_INTERFACE_NAME}d.lib)

# add interface
add_library(${TI_INTERFACE_NAME} SHARED ${TI_INTERFACE_SOURCES} ${TI_INTERFACE_HEADERS})
target_link_libraries(${TI_INTERFACE_NAME} ${OpenCV_LIBS})
message(STATUS "opencv: ${OpenCV_LIBS}")
 
# interface flags
set_target_properties(${TI_INTERFACE_NAME} PROPERTIES ARCHIVE_OUTPUT_DIRECTORY_DEBUG ${CMAKE_BINARY_DIR}/libs)
set_target_properties(${TI_INTERFACE_NAME} PROPERTIES ARCHIVE_OUTPUT_DIRECTORY_RELEASE ${CMAKE_BINARY_DIR}/libs)
set_target_properties(${TI_INTERFACE_NAME} PROPERTIES ARCHIVE_OUTPUT_DIRECTORY_RELWITHDEBINFO ${CMAKE_BINARY_DIR}/libs)

#set_target_properties(${TI_INTERFACE_NAME} PROPERTIES COMPILE_FLAGS "-DDK_LOADER_DLL_EXPORT -DNOMINMAX")
set_target_properties(${TI_INTERFACE_NAME} PROPERTIES DEBUG_OUTPUT_NAME ${TI_INTERFACE_NAME}d)
set_target_properties(${TI_INTERFACE_NAME} PROPERTIES RELEASE_OUTPUT_NAME ${TI_INTERFACE_NAME})
 
set_property(TARGET ${TI_INTERFACE_NAME} PROPERTY VERSION ${TI_VERSION_MAJOR}.${NOMACS_VERSION_MINOR}.${NOMACS_VERSION_PATCH})
set_property(TARGET ${TI_INTERFACE_NAME} PROPERTY SOVERSION ${TI_VERSION_MAJOR})

# add test
add_executable(${TI_TEST_NAME} WIN32  MACOSX_BUNDLE ${TI_TEST_SOURCES} ${TI_TEST_HEADERS})
target_link_libraries(${TI_TEST_NAME} ${LIB_INTERFACE_NAME} ${OpenCV_LIBS}) 

# add example plugin
add_executable(${TI_EXAMPLE_NAME} WIN32  MACOSX_BUNDLE ${TI_EXAMPLE_SOURCES} ${TI_EXAMPLE_HEADERS})
target_link_libraries(${TI_EXAMPLE_NAME} ${LIB_INTERFACE_NAME} ${OpenCV_LIBS}) 

add_dependencies(${TI_TEST_NAME} ${TI_INTERFACE_NAME})
add_dependencies(${TI_EXAMPLE_NAME} ${TI_INTERFACE_NAME})

# target_include_directories(${TI_INTERFACE_NAME} PRIVATE ${OpenCV_INCLUDE_DIRS})
# target_include_directories(${TI_TEST_NAME} 		PRIVATE ${OpenCV_INCLUDE_DIRS})
# target_include_directories(${TI_EXAMPLE_NAME} 	PRIVATE ${OpenCV_INCLUDE_DIRS})

endmacro(TI_CREATE_TARGETS)

macro(TI_FIND_OPENCV)

find_package(OpenCV REQUIRED core imgproc)

#TODO reveiw next line
SET(OpenCV_LIBS ${OpenCV_LIBRARY_DIRS})# ${OpenCV_LIB_DIR_DBG} ${OpenCV_LIB_DIR_OPT} ${OpenCV_DIR}/lib/${OpenCV_LIB_DIR_DBG} ${OpenCV_DIR}/lib/${OpenCV_LIB_DIR_OPT})

if(NOT OpenCV_FOUND)
	message(FATAL_ERROR "OpenCV not found.") 
else()
	add_definitions(-DWITH_OPENCV)
endif()

# unset include directories since OpenCV sets them global
get_property(the_include_dirs  DIRECTORY . PROPERTY INCLUDE_DIRECTORIES)
list(REMOVE_ITEM the_include_dirs ${OpenCV_INCLUDE_DIRS})
set_property(DIRECTORY . PROPERTY INCLUDE_DIRECTORIES ${the_include_dirs})

endmacro(TI_FIND_OPENCV)