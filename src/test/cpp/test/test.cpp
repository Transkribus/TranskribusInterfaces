
#include <iostream>
#include <curl/curl.h>
#include <curl/easy.h>
#include "opencv2/core/core.hpp"

using namespace std;
using namespace cv;
using namespace curl;

bool loadImage(string inputName, Mat &image)
{
  bool from_net;
  from_net = true;


  if (inputName.find("http") != string::npos)
    {
      string URL;
      URL = inputName;
      if (inputName.find("\"") == 0)
        {
           URL = inputName.substr(1,inputName.length()-2);
        }

  std::ostringstream stream;

  curl_writer writer(stream);
  // Pass it to the easy constructor and watch the content returned in that file!
  curl_easy easy(writer);

  // Add some option to the easy handle
  easy.add(curl_pair<CURLoption,string>(CURLOPT_URL,URL));
  easy.add(curl_pair<CURLoption,long>(CURLOPT_FOLLOWLOCATION,1L));

  try {
    easy.perform();
  } catch (curl_easy_exception error) {
    // If you want to get the entire error stack we can do:
    vector<pair<string,string>> errors = error.what();
    // Otherwise we could print the stack like this:
    error.print_traceback();
  }
  string output = stream.str(); // convert the stream into a string
  if (output.find("404 Not Found") != string::npos)
    from_net = false;
  else
  {
      vector<char> data = std::vector<char>( output.begin(), output.end() ); //convert string into a vector
  if (data.size() > 0)
    {
      Mat data_mat = Mat(data); // create the cv::Mat datatype from the vector
      image = imdecode(data_mat,-1); //read an image from memory buffer
      if(image.rows == 0 || image.cols == 0)
    from_net = false;
    }
  else
    from_net = false;
    }
}
  else
    {
      image = imread( inputName, 1 );
      if (image.total() < 1)
        from_net = false;
    }
  return from_net;
}

int main(int argc, char** argv) {
	cout << "hello test world" << endl;

	Mat image;
	loadImage("", image);

}
