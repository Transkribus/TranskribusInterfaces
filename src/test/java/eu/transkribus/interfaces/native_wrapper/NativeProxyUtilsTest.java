package eu.transkribus.interfaces.native_wrapper;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import eu.transkribus.interfaces.types.Image;

public class NativeProxyUtilsTest {

	@Test
	public void testToNativeImage() throws IOException {
		File imgFile = new File("src/test/resources/img/035_320_001.jpg");
		Image img = new Image(ImageIO.read(imgFile));
		NativeProxyUtils.toNativeImage(img);
	}
}
