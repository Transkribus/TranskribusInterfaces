package eu.transkribus.interfaces.native_wrapper;

import eu.transkribus.interfaces.IModule;
import eu.transkribus.interfaces.native_wrapper.swig.Native_IModule;
import eu.transkribus.interfaces.native_wrapper.swig.Native_ModuleFactory;

public abstract class NativeModuleProxy implements IModule {
	
	Native_IModule module;
		
	public NativeModuleProxy(String pathToPluginLib, String[] pars) {
		module = Native_ModuleFactory.createModuleFromLib(pathToPluginLib, NativeProxyUtils.toStringVector(pars));		
	}
	
	@Override public String usage() {
		return module.usage();
	}

	@Override public String getToolName() {
		return module.getToolName();
	}

	@Override public String getVersion() {
		return module.getVersion();
	}

	@Override public String getProvider() {
		return module.getProvider();
	}
	
}
