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

    @Override
    public void createTrainData(String[] pageXmls, String outputDir, String pathToCharacterMap, String[] props) {
        trainHtr.createTrainData(NativeProxyUtils.toStringVector(pageXmls), outputDir, pathToCharacterMap, NativeProxyUtils.toStringVector(props));
    }

    @Override
    public void trainHtr(String pathToModelsIn, String pathToModelsOut, String inputTrainDir, String inputValDir, String[] props) {
        trainHtr.trainHtr(pathToModelsIn, pathToModelsOut, inputTrainDir, inputValDir, NativeProxyUtils.toStringVector(props));

    }

    @Override
    public void createHtr(String pathToModelsOut, String pathToCharMapFile, String[] props) {
        trainHtr.createHtr(pathToModelsOut, pathToCharMapFile, NativeProxyUtils.toStringVector(props));
    }

}
