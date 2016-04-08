import eu.transkribus.interfaces.types.util.ImageUtils;
import eu.transkribus.native_interfaces.ILayoutAnalysis;
import eu.transkribus.native_interfaces.Image;
import eu.transkribus.native_interfaces.MyLayoutAnalysis;
import eu.transkribus.native_interfaces.StringVector;

public class TestNativeInterfaces {
	
	
	public static void main(String[] args) throws Exception {
		String swigDir = System.getProperty("user.dir")+"/src/main/swig/";
		
//		ImageUtils.setLibraryPath("/home/sebastianc/workspace/TranskribusInterfaces/src/main/swig"+":"+System.getProperty("java.library.path"));
		ImageUtils.setLibraryPath(swigDir+":"+System.getProperty("java.library.path"));
//		System.out.println("libpath = "+System.getProperty("java.library.path"));
		
//		System.out.println("cwd: "+System.getProperty("user.dir"));
		
//		System.load("/home/sebastianc/workspace/TranskribusInterfaces/src/main/swig/libMyLayoutAnalysis.so");
		System.load(System.getProperty("user.dir")+"/src/main/swig/libTranskribusInterfaces.so");
		
		ILayoutAnalysis a = new MyLayoutAnalysis();
		Image i = new Image("adsf");

		StringVector sv = new StringVector();
		
		
		
		System.out.println(a.usage());
		System.out.println(a.getProvider());
		System.out.println(a.getToolName());
		System.out.println(a.getVersion());
		
		a.process(i, "bla", "bla", sv, sv);
		a.processLayout(i, "xmlin", "xmlout");
		
	}

}
