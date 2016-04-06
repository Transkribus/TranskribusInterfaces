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

%{
#include "../cpp/Image.h"
%}

/* Let's just grab the original header file here */
%include "../cpp/Image.h"