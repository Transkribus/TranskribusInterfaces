/* File : image.i */
%module image

%include "std_string.i"
%apply const std::string& {std::string* foo};

%include "exception.i"

/* map c++ Mat type to java Mat type */
%typemap(jstype) cv::Mat& "org.opencv.core.Mat"
%typemap(javain) cv::Mat& "$javainput.getNativeObjAddr()"
%typemap(jtype) cv::Mat& "long"
%typemap(jni) cv::Mat& "jlong"
%typemap(in) cv::Mat& {
        $1 = *(cv::Mat **)&$input;
}

%exception {
  try {
    $action
  } catch (const std::exception& e) {
    SWIG_exception(SWIG_RuntimeError, e.what());
  }
}

%ignore transkribus::Image::mat;
%ignore transkribus::Image::url;

%ignore transkribus::operator<<(std::ostream& os, const Image& image);

%extend transkribus::Image {
public:
	const std::string& transkribus::getUrl() { return url; }
	const cv::Mat& transkribus::getMat() { return mat; }
};

%{
#include "../cpp/Image.h"
%}

%rename (Native_Image) Image;

/* Let's just grab the original header file here */
%include "../cpp/Image.h"