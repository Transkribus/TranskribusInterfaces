package eu.transkribus.interfaces.native_wrapper;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import eu.transkribus.interfaces.types.Image;
import eu.transkribus.interfaces.types.util.SysPathUtils;

public class NativeProxyUtilsTest {

	@Before
	public void setupEnvironment() {
		SysPathUtils.addDirToPath("/usr/local/lib");
		SysPathUtils.addDirToPath("/usr/local/share/OpenCV/java");
		Image.registerImageIOServices();
	}
	
	@Test
	public void testToNativeImage() throws IOException {
		File imgFile = new File("src/test/resources/img/035_320_001.jpg");
		Image img = new Image(ImageIO.read(imgFile));
		NativeProxyUtils.toNativeImage(img);
	}
}
