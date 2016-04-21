package eu.transkribus.interfaces;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class ModuleFactoryTest {
	@Test public void testMyLayoutAnalysis() throws Exception {
		URL jarUrl = null; // if null, default classloader is used
		String[] pars = null;
		
		IModule module = ModuleFactory.createFromJar(null, "eu.transkribus.interfaces.example_plugin.MyModuleFactory", null);
		System.out.println("module provider = "+module.getProvider());
		
		Assert.assertSame("expected module provider does match!", "myProvider", module.getProvider());
		
		ILayoutAnalysis la = (ILayoutAnalysis) module;
		
		la.process(null, null, null, null);
	}

}
