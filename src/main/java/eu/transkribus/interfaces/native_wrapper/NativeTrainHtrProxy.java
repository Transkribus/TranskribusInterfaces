package eu.transkribus.interfaces.native_wrapper;

import eu.transkribus.interfaces.IBaseline2Polygon;
import eu.transkribus.interfaces.ITrainHtr;
import eu.transkribus.interfaces.native_wrapper.swig.Native_ITrainHtr;
import eu.transkribus.interfaces.native_wrapper.swig.Native_ModuleFactory;

/**
 * @deprecated FIXME: IBaseline2Polygon's implemented in java CANNOT be called this way in C++ code!!! 
 * possible workaround: check if IBaseline2Polygon is a java implementation and throw exception if it is!
 */
public class NativeTrainHtrProxy extends NativeModuleProxy implements ITrainHtr {
	
	Native_ITrainHtr trainHtr;
	
	public NativeTrainHtrProxy(String pathToPluginLib, String[] pars) {
		super(pathToPluginLib, pars);
		trainHtr = Native_ModuleFactory.castITrainHtr(module);
	}

	@Override public void trainHtr(String pathToModelsIn, String pathToModelsOut, String[] props, String inputDir) {
		trainHtr.trainHtr(pathToModelsIn, pathToModelsOut, NativeProxyUtils.toStringVector(props), inputDir);
	}

	@Override public void createTrainData(String[] pageXmls, String outputDir, IBaseline2Polygon mapper) {
		//FIXME: IBaseline2Polygon's implemented in java CANNOT be called this way in C++ code!!! 
		//  workaround: check if IBaseline2Polygon is a java implementation and throw exception if it is!
		trainHtr.createTrainData(NativeProxyUtils.toStringVector(pageXmls), outputDir, null);
	}
	
	
}
