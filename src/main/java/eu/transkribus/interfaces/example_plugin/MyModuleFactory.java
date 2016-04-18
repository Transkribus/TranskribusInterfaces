package eu.transkribus.interfaces.example_plugin;

import eu.transkribus.interfaces.IModule;
import eu.transkribus.interfaces.ModuleFactory;

public class MyModuleFactory extends ModuleFactory {
	
	@Override public IModule create(String[] pars) {
		System.out.println("Creating new MyLayoutAnalysis!");
		return new MyLayoutAnalysis();
	}

}


