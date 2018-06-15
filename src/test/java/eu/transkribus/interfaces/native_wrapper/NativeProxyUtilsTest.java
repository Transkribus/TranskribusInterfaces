package eu.transkribus.interfaces.native_wrapper;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import eu.transkribus.interfaces.types.Image;
import eu.transkribus.interfaces.util.NativeLibRegistry;

public class NativeProxyUtilsTest {

	@Before
	public void setupEnvironment() {
		/*
		 * If loading the interfaces wrapper fails, try exporting LD_LIBRARY_PATH=/usr/local/lib
		 */
		NativeLibRegistry.INSTANCE.addDirsToPath("/usr/local/share/OpenCV/java/");
		NativeLibRegistry.INSTANCE.loadTranskribusInterfacesLib();
		NativeLibRegistry.INSTANCE.loadLibrary("opencv_java310");
	}
	
	@Test
	public void testToNativeImage() throws IOException {
		File imgFile = new File("src/test/resources/img/035_320_001.jpg");
		Image img = new Image(ImageIO.read(imgFile));
		NativeProxyUtils.toNativeImage(img);
	}
}
