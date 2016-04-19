package eu.transkribus.interfaces.native_wrapper;

import java.net.MalformedURLException;
import java.net.URL;

import eu.transkribus.interfaces.ILayoutAnalysis;
import eu.transkribus.interfaces.types.Image;
import eu.transkribus.interfaces.types.util.ImageUtils;
import eu.transkribus.interfaces.types.util.SysPathUtils;

public class TestNativeInterfaces {
	
	public static void testLayoutAnalysis(ILayoutAnalysis la) throws MalformedURLException {
		Image i = new Image(new URL("http://www.austriatraveldirect.com/files/INNNORD01041.jpg"));
		System.out.println(la.usage());
		System.out.println(la.getProvider());
		System.out.println(la.getToolName());
		System.out.println(la.getVersion());
		
		la.process(i, "bla1", null, null);
	}
	
	public static void main(String[] args) throws Exception {
		String resourcesDir = System.getProperty("user.dir")+"/src/main/resources/";
		SysPathUtils.addDirToPath(resourcesDir);
		System.out.println("libpath = "+SysPathUtils.getPath());
		
		System.loadLibrary("TranskribusInterfacesWrapper");
//		System.load(System.getProperty("user.dir")+"/src/main/resources/libTranskribusInterfaces.so");
		
		String pluginPath = "./src/main/resources/libMyLayoutAnalysis.so";
		ILayoutAnalysis la = new NativeLayoutAnalysisProxy(pluginPath, null);
		
		testLayoutAnalysis(la);
	}

}
