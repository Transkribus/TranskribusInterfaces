package eu.transkribus.interfaces;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Factory class that shall be extended by Java based modules wrapping the constructor using some pars.
 * See {@link eu.transkribus.interfaces.example_plugin} for an example implementation
 */
public class ModuleFactory {
	protected static ModuleFactory instance = null;
	private static final Class<ModuleFactory> CLAZZ = ModuleFactory.class; 
		
	public IModule create(String[] pars) {
		return null;
	}
	
	public static IModule createFromJar(URL jarUrl, String factoryClassName, String[] pars) throws InstantiationException, IllegalAccessException, ClassNotFoundException {		
		ClassLoader loader = jarUrl != null ? 
				URLClassLoader.newInstance( new URL[] {jarUrl} , CLAZZ.getClassLoader()) : CLAZZ.getClassLoader();
		
		Class<?> clazz = Class.forName(factoryClassName, true, loader);
		Class<? extends ModuleFactory> moduleClass = clazz.asSubclass(CLAZZ);

		ModuleFactory factory = moduleClass.newInstance();
		
		return factory.create(pars);
	}
	
}
