macro(TI_CREATE_TARGETS)
	# create the targets
	set(TI_TEST_NAME 		Test${PROJECT_NAME})
	set(TI_INTERFACE_NAME 	${PROJECT_NAME})

	set(LIB_INTERFACE_NAME	optimized ${TI_INTERFACE_NAME}.lib debug ${TI_INTERFACE_NAME}d.lib)

	set(LIBRARY_DIR ${CMAKE_CURRENT_BINARY_DIR}/libs ${CMAKE_CURRENT_BINARY_DIR}) #add libs directory to library dirs
	link_directories(${LIBRARY_DIR})

	set_target_properties(${OpenCV_LIBS} PROPERTIES MAP_IMPORTED_CONFIG_RELWITHDEBINFO RELEASE)

	# add interface
	add_library(${TI_INTERFACE_NAME} SHARED ${TI_INTERFACE_SOURCES} ${TI_INTERFACE_HEADERS})
	target_link_libraries(${TI_INTERFACE_NAME} ${OpenCV_LIBS} ${CURL_LIBRARY} TranskribusInterfaces ${RDF_LIBS})
	add_dependencies(${TI_INTERFACE_NAME} TranskribusInterfaces)

	# interface flags
	set_target_properties(${TI_INTERFACE_NAME} PROPERTIES ARCHIVE_OUTPUT_DIRECTORY_DEBUG ${CMAKE_BINARY_DIR}/libs)
	set_target_properties(${TI_INTERFACE_NAME} PROPERTIES ARCHIVE_OUTPUT_DIRECTORY_RELEASE ${CMAKE_BINARY_DIR}/libs)
	set_target_properties(${TI_INTERFACE_NAME} PROPERTIES ARCHIVE_OUTPUT_DIRECTORY_RELWITHDEBINFO ${CMAKE_BINARY_DIR}/libs)

	if(MSVC) # linux does not need this
		set_target_properties(${TI_INTERFACE_NAME} PROPERTIES COMPILE_FLAGS "-DTI_DLL_EXPORT")
	else() # enable soname
    set_property(TARGET ${TI_INTERFACE_NAME} PROPERTY VERSION ${TI_VERSION_MAJOR}.${TI_VERSION_MINOR}.${TI_VERSION_PATCH})
    set_property(TARGET ${TI_INTERFACE_NAME} PROPERTY SOVERSION ${TI_VERSION_MAJOR})
	endif()
	set_target_properties(${TI_INTERFACE_NAME} PROPERTIES DEBUG_OUTPUT_NAME ${TI_INTERFACE_NAME}d)
	set_target_properties(${TI_INTERFACE_NAME} PROPERTIES RELEASE_OUTPUT_NAME ${TI_INTERFACE_NAME})

	set(TI_INTERFACE_LIB_PATH ${CMAKE_CURRENT_BINARY_DIR}/libs/${TI_INTERFACE_NAME})

	set_property(TARGET ${TI_INTERFACE_NAME} PROPERTY VERSION ${TI_VERSION_MAJOR}.${TI_VERSION_MINOR}.${TI_VERSION_PATCH})
	set_property(TARGET ${TI_INTERFACE_NAME} PROPERTY SOVERSION ${TI_VERSION_MAJOR})

	# add test
	add_executable(${TI_TEST_NAME} WIN32  MACOSX_BUNDLE ${TI_TEST_SOURCES} ${TI_TEST_HEADERS})
	target_link_libraries(${TI_TEST_NAME} ${TI_INTERFACE_NAME} ${OpenCV_LIBS} ${CURL_LIBRARY} TranskribusInterfaces)
	set_target_properties(${TI_TEST_NAME} PROPERTIES LINK_FLAGS "/SUBSYSTEM:CONSOLE")
	add_dependencies(${TI_TEST_NAME} ${TI_INTERFACE_NAME} TranskribusInterfaces)
	
	target_include_directories(${TI_INTERFACE_NAME} PRIVATE ${OpenCV_INCLUDE_DIRS} ${CURL_INCLUDE})
	target_include_directories(${TI_TEST_NAME} 		PRIVATE ${OpenCV_INCLUDE_DIRS} ${CURL_INCLUDE})
	
	qt5_use_modules(${TI_INTERFACE_NAME} 		Core)
	qt5_use_modules(${TI_TEST_NAME} 		Core)
	
	if (MSVC)
		### DependencyCollector
		set(DC_SCRIPT ${CMAKE_SOURCE_DIR}/cmake/DependencyCollector.py)
		set(DC_CONFIG ${CMAKE_CURRENT_BINARY_DIR}/DependencyCollector.ini)

		GET_FILENAME_COMPONENT(VS_PATH ${CMAKE_LINKER} PATH)
		if(CMAKE_CL_64)
			SET(VS_PATH "${VS_PATH}/../../../Common7/IDE/Remote Debugger/x64")
		else()
			SET(VS_PATH "${VS_PATH}/../../Common7/IDE/Remote Debugger/x86")
		endif()
		SET(DC_PATHS_RELEASE ${OpenCV_DIR}/bin/Release ${QT_QMAKE_PATH} ${VS_PATH} ${ReadFramework_DIR}/Release)
		SET(DC_PATHS_DEBUG ${OpenCV_DIR}/bin/Debug ${QT_QMAKE_PATH} ${VS_PATH} ${ReadFramework_DIR}/Debug)

		configure_file(${CMAKE_SOURCE_DIR}/cmake/DependencyCollector.config.cmake.in ${DC_CONFIG})
		
		add_custom_command(TARGET ${TI_INTERFACE_NAME} POST_BUILD COMMAND ${DC_SCRIPT} --infile $<TARGET_FILE:${PROJECT_NAME}> --configfile ${DC_CONFIG} --configuration $<CONFIGURATION>)		
		add_custom_command(TARGET ${TI_TEST_NAME} POST_BUILD COMMAND ${DC_SCRIPT} --infile $<TARGET_FILE:${PROJECT_NAME}> --configfile ${DC_CONFIG} --configuration $<CONFIGURATION>)		
	endif(MSVC)
endmacro(TI_CREATE_TARGETS)

# Searches for OpenCV
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
			message(STATUS "building without highgui")
		ENDIF()

		message(STATUS "OpenCV Modules ${OpenCV_LIBS} added...")
	ENDIF()
endmacro(TI_FIND_OPENCV)

# Searches for Qt with the required components
macro(TI_FIND_QT)
	set(CMAKE_AUTOMOC ON)
	set(CMAKE_AUTORCC OFF)

	set(CMAKE_INCLUDE_CURRENT_DIR ON)
	if(NOT QT_QMAKE_EXECUTABLE)
	 find_program(QT_QMAKE_EXECUTABLE NAMES "qmake" "qmake-qt5" "qmake.exe")
	endif()
	if(NOT QT_QMAKE_EXECUTABLE)
		message(FATAL_ERROR "you have to set the path to the Qt5 qmake executable")
	endif()
	message(STATUS "QMake found: path: ${QT_QMAKE_EXECUTABLE}")
	GET_FILENAME_COMPONENT(QT_QMAKE_PATH ${QT_QMAKE_EXECUTABLE} PATH)
	set(QT_ROOT ${QT_QMAKE_PATH}/)
	SET(CMAKE_PREFIX_PATH ${CMAKE_PREFIX_PATH} ${QT_QMAKE_PATH}\\..\\lib\\cmake\\Qt5)
	find_package(Qt5 REQUIRED Core Network LinguistTools)
	if (NOT Qt5_FOUND)
		message(FATAL_ERROR "Qt5 not found. Check your QT_QMAKE_EXECUTABLE path and set it to the correct location")
	endif()
endmacro(TI_FIND_QT)

# Searches for CVL ReadFramework
macro(TI_FIND_RDF)

	set(CMAKE_PREFIX_PATH ${CMAKE_PREFIX_PATH} ${CMAKE_BINARY_DIR})
	find_package(ReadFramework)

	if(NOT RDF_FOUND)
		set(RDF_BUILD_DIRECTORY "NOT_SET" CACHE PATH "Path to the READ Framework build directory")
		if(${RDF_BUILD_DIRECTORY} STREQUAL "NOT_SET")
			message(FATAL_ERROR "You have to set the READ Framework build directory")
		endif()
	else()
		# CVL ReadFramework depends on Qt and OpenCV
		TI_FIND_QT()
	endif(NOT RDF_FOUND)
endmacro(TI_FIND_RDF)


macro(TI_FIND_CURL)

	if (WITH_CURL)
		# add user prefix paths
		if (IS_DIRECTORY ${CURL_INCLUDE})
			SET(CMAKE_PREFIX_PATH ${CMAKE_PREFIX_PATH} ${CURL_INCLUDE})
			message(STATUS "${CURL_INCLUDE} set as cUrl include path")
		endif()

		if (IS_DIRECTORY ${CURL_BUILD_DIR})
			SET(CMAKE_PREFIX_PATH ${CMAKE_PREFIX_PATH} ${CURL_BUILD_DIR}/lib/Release)
			SET(CURL_INCLUDE ${CURL_INCLUDE} ${CURL_BUILD_DIR}/include/curl)
			message(STATUS "${CURL_BUILD_DIR} set as cUrl build path")
		endif()

		if (MSVC)
			find_package(curl REQUIRED)
		else()
			find_package(CURL REQUIRED)
		endif()

		if (NOT CURL_FOUND)

			# let the user set the cURL include path
			SET(CURL_INCLUDE "cURL include path" CACHE PATH "choose your cUrl path")

			# let the user set the cURL library path
			SET(CURL_BUILD_DIR "cURL build path" CACHE PATH "choose your cUrl build path")

			message(FATAL_ERROR "could not find cURL although it is requested - fix the paths or uncheck WITH_CURL")

		endif()
	else()
			add_definitions(-DWITHOUT_CURL)
			message(STATUS "building without cURL")
	endif() # WITH_CURL
endmacro(TI_FIND_CURL)
