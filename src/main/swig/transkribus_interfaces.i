/* File : transkribus_interfaces.i */
%module transkribus_interfaces

%include "image.i"
/*%include "arrays_java.i";*/

%include "std_vector.i"

namespace std {
   %template(StringVector) vector<string>;
};

%include "various.i"

%apply char **STRING_ARRAY { char **db }

%{
#include "../cpp/ILayoutAnalysis.h"
#include "../cpp/test1/MyLayoutAnalysis.h"
%}

/* Let's just grab the original header file here */
%include "../cpp/ILayoutAnalysis.h"
%include "../cpp/test1/MyLayoutAnalysis.h"