package eu.transkribus.interfaces.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum NativeLibRegistry {
	INSTANCE;
	private static final Logger logger = LoggerFactory.getLogger(NativeLibRegistry.class);
	
	private final List<String> loadedLibs;
	
	public final static String TRANSKRIBUS_INTERFACES_WRAPPER_NAME = "TranskribusInterfacesWrapper";
	
	private NativeLibRegistry() {
		loadedLibs = new ArrayList<>(0);
		SysPathUtils.addDirToPath("/usr/local/lib", true);
	}
	
	public List<String> getLoadedLibs() {
		List<String> copy = new ArrayList<>(loadedLibs.size());
		Collections.copy(copy, loadedLibs);
		return copy;
	}
	
	public void addDirsToPath(String... paths) {
		if(paths == null || paths.length == 0) {
			return;
		}
		for(String p : paths) {
			if(p == null || p.isEmpty()) {
				continue;
			}
			SysPathUtils.addDirToPath(p, true);
		}
	}
	
	public void loadTranskribusInterfacesLib() {
		loadLibrary(TRANSKRIBUS_INTERFACES_WRAPPER_NAME);
	}
	
	public void loadLibrary(final String libName) {
		if(libName == null || libName.isEmpty()) {
			logger.warn("An empty library name was passed! Doing nothing.");
			return;
		}
		if(loadedLibs.contains(libName)) {
			logger.warn("Library is already loaded: " + libName);
			return;
		}
		try {
			System.loadLibrary(libName);
			loadedLibs.add(libName);
			logger.info("Loaded lib: " + libName);
		} catch (UnsatisfiedLinkError e) {
			throw new RuntimeException("Could not load "+libName+".so: " + e.getMessage(), e);
		}
	}
}
