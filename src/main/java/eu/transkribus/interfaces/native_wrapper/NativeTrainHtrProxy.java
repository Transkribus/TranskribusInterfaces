package eu.transkribus.interfaces.native_wrapper;

import eu.transkribus.interfaces.ITrainHtr;
import eu.transkribus.interfaces.native_wrapper.swig.Native_ITrainHtr;
import eu.transkribus.interfaces.native_wrapper.swig.Native_ModuleFactory;

public class NativeTrainHtrProxy extends NativeModuleProxy implements ITrainHtr {
	
	Native_ITrainHtr trainHtr;
	
	public NativeTrainHtrProxy(String pathToPluginLib, String[] pars) {
		super(pathToPluginLib, pars);
		trainHtr = Native_ModuleFactory.castITrainHtr(module);
	}

	@Override public void trainHtr(String pathToModelsIn, String pathToModelsOut, String[] props, String inputDir) {
		trainHtr.trainHtr(pathToModelsIn, pathToModelsOut, NativeProxyUtils.toStringVector(props), inputDir);
	}

	@Override public void createTrainData(String[] pageXmls, String outputDir, String[] props) {
		trainHtr.createTrainData(NativeProxyUtils.toStringVector(pageXmls), outputDir, NativeProxyUtils.toStringVector(props));
	}
	
	
}
