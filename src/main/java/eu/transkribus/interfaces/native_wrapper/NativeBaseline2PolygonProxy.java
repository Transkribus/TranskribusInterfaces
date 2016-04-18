package eu.transkribus.interfaces.native_wrapper;

import java.io.IOException;

import eu.transkribus.interfaces.ILayoutAnalysis;
import eu.transkribus.interfaces.native_wrapper.swig.Native_IBaseline2Polygon;
import eu.transkribus.interfaces.native_wrapper.swig.Native_ModuleFactory;
import eu.transkribus.interfaces.types.Image;

public class NativeBaseline2PolygonProxy extends NativeModuleProxy implements ILayoutAnalysis {
	
	Native_IBaseline2Polygon bl;
		
	public NativeBaseline2PolygonProxy(String pathToPluginLib, String[] pars) {
		super(pathToPluginLib, pars);
		bl = Native_ModuleFactory.castIBaseline2Coords(module);
	}
	
	@Override public void process(Image image, String xmlInOut, String[] ids, String[] props) {		
		try {
			bl.process(NativeProxyUtils.toNativeImage(image), xmlInOut, NativeProxyUtils.toStringVector(ids), NativeProxyUtils.toStringVector(props));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
