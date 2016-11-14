package eu.transkribus.interfaces.native_wrapper;

import java.io.IOException;

import eu.transkribus.interfaces.IHtr;
import eu.transkribus.interfaces.native_wrapper.swig.Native_IHtr;
import eu.transkribus.interfaces.native_wrapper.swig.Native_ModuleFactory;
import eu.transkribus.interfaces.types.Image;

public class NativeHtrProxy extends NativeModuleProxy implements IHtr {

    Native_IHtr htr;

    public NativeHtrProxy(String pathToPluginLib, String[] pars) {
        super(pathToPluginLib, pars);
        htr = Native_ModuleFactory.castIHtr(module);
    }

    @Override
    public void process(
            String pathToOpticalModel,
            String pathToLanguageModel,
            String pathToCharacterMap,
            Image image,
            String xmlInOut,
            String storageDir,
            String[] lineIds,
            String[] props
    ) {
        try {
            htr.process(pathToOpticalModel, pathToLanguageModel, pathToCharacterMap, NativeProxyUtils.toNativeImage(image), xmlInOut, storageDir, NativeProxyUtils.toStringVector(lineIds), NativeProxyUtils.toStringVector(props));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
