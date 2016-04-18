package eu.transkribus.interfaces.native_wrapper;

import java.io.IOException;

import eu.transkribus.interfaces.native_wrapper.swig.Native_IBaseline2Polygon;
import eu.transkribus.interfaces.native_wrapper.swig.Native_Image;
import eu.transkribus.interfaces.native_wrapper.swig.StringVector;
import eu.transkribus.interfaces.types.Image;
import eu.transkribus.interfaces.types.Image.Type;

public class NativeProxyUtils {
			
	public static StringVector toStringVector(String... strs) {
		StringVector s = new StringVector();		
		if (strs != null) {
			for (String str : strs) {
				s.add(str);
			}
		}
		
		return s;
	}
	
	
	public static Native_Image toNativeImage(Image img) throws IOException {
		if (img.hasType(Type.OPEN_CV)) {
			return new Native_Image(img.getImageOpenCVImage());
		}
		else if (img.hasType(Type.JAVA)) {
			img.createType(Type.OPEN_CV);
			return new Native_Image(img.getImageOpenCVImage());
		}
		else if (img.hasType(Type.URL)) {
			return new Native_Image(img.getImageUrl().toString());
		}
		
		throw new IOException("Cannot convert to native image type - existing types are: "+img.getAvailableTypesString());
	}

}
