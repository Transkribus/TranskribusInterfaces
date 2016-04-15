/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.5
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package eu.transkribus.interfaces.native_wrapper.swig;

public class Native_Image {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected Native_Image(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Native_Image obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        transkribus_interfacesJNI.delete_Native_Image(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setUrl(String value) {
    transkribus_interfacesJNI.Native_Image_url_set(swigCPtr, this, value);
  }

  public String getUrl() {
    return transkribus_interfacesJNI.Native_Image_url_get(swigCPtr, this);
  }

  public void setMat(SWIGTYPE_p_cv__Mat value) {
    transkribus_interfacesJNI.Native_Image_mat_set(swigCPtr, this, SWIGTYPE_p_cv__Mat.getCPtr(value));
  }

  public SWIGTYPE_p_cv__Mat getMat() {
    return new SWIGTYPE_p_cv__Mat(transkribus_interfacesJNI.Native_Image_mat_get(swigCPtr, this), true);
  }

  public Native_Image(Native_Image image) {
    this(transkribus_interfacesJNI.new_Native_Image__SWIG_0(Native_Image.getCPtr(image), image), true);
  }

  public Native_Image(String url) {
    this(transkribus_interfacesJNI.new_Native_Image__SWIG_2(url), true);
  }

  public Native_Image(org.opencv.core.Mat mat) {
    this(transkribus_interfacesJNI.new_Native_Image__SWIG_3(mat.getNativeObjAddr()), true);
  }

  public void display() {
    transkribus_interfacesJNI.Native_Image_display(swigCPtr, this);
  }

  public int getWidth() {
    return transkribus_interfacesJNI.Native_Image_getWidth(swigCPtr, this);
  }

  public int getHeight() {
    return transkribus_interfacesJNI.Native_Image_getHeight(swigCPtr, this);
  }

}
