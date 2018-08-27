package eu.transkribus.interfaces.types;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.transkribus.interfaces.types.Image.Type;
import eu.transkribus.interfaces.types.util.ImageUtils;

public class ImageTest {

	private static final Logger logger = LoggerFactory.getLogger(ImageTest.class);
	
//	@Before
//	public void init() {
//		ImageUtils.listImageIOServices();
//		ImageUtils.registerImageIOServices();
//		ImageUtils.listImageIOServices();
//	}
	
	@Test
	public void testReaders() throws IOException {

		ImageUtils.testReaders();

	}

	@Test
	public void testFromUrlToOpenCv() throws IOException {
		logger.debug("testFromUrlToOpenCv");

		URL[] urls = { new URL("http://www.austriatraveldirect.com/files/INNNORD01041.jpg"),
				new URL("http://dbis-thure.uibk.ac.at/f/Get?id=UNKRNHSATTZGUUMKZBSBNOUC"),
				new URL("https://files-test.transkribus.eu/Get?fileType=bin&id=CFELMFJLDLMWBFVUUDQTTMXR") };

		for (URL url : urls) {
			Image img = new Image(url);
			img.createType(Type.OPEN_CV);

			logger.info("Created opencv image for url: " + url);
		}
	}
	
	@Test
	public void testFromUrlToBufferedImage() throws IOException {
		logger.debug("testFromUrlToBufferedImage");

		URL[] urls = { new URL("https://dbis-thure.uibk.ac.at/f/Get?fileType=orig&id=VJCQMQBNZFDFCZMAJZHKNJKW") };

		for (URL url : urls) {
			Image img = new Image(url);
			try {
				img.createType(Type.JAVA);
			} catch (Exception e) {
				logger.error("Could not create BufferedImage from URL resource!", e);
				throw e;
			}

			logger.info("Created java image for url: " + url);
		}

	}
	
	/**
	 * A main for testing the behavior without JUnit
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new ImageTest().testFromUrlToBufferedImage();
	}
}
