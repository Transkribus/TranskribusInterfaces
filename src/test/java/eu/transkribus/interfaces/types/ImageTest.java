package eu.transkribus.interfaces.types;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.Mat;
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
	
	@Test
	public void testImageOrientationJpg() throws IOException {
		URL imgUrl = new URL("https://files-test.transkribus.eu/Get?id=XBNTVRNUTFEDVFPPGIGIJRSN&fileType=view");
		
		logger.debug("Using file: " + imgUrl);
		
		BufferedImage bi2 = testImageOrientation(imgUrl);
		
		try (OutputStream output = new FileOutputStream(new File("/tmp/test.jpg"))) {
			ImageIO.write(bi2, "jpg", output);
		}
	}
	
//	@Test
//	public void testImageOrientationTif() throws IOException {
//		URL imgUrl = ImageTest.class.getClassLoader().getResource("img/exif_orientation_6.tif");
//		
//		BufferedImage bi2 = testImageOrientation(imgUrl);
//		
//		try (OutputStream output = new FileOutputStream(new File("/tmp/test.tif"))) {
//			ImageIO.write(bi2, "tif", output);
//		}
//	}

	public BufferedImage testImageOrientation(URL imgUrl) throws IOException {
		logger.debug("Test to load an image with exif orientation set: " + imgUrl);
		BufferedImage bi = ImageIO.read(imgUrl);
		Image img = new Image(imgUrl);
		BufferedImage bi2 = img.getImageBufferedImage(true);
		
		int xRes = bi2.getWidth();
		final int xResWhenCorrectlyRotated = bi.getHeight();
		Assert.assertEquals("Image orientation is incorrect. Width does not match expected value.", xResWhenCorrectlyRotated, xRes);
		
		int yRes = bi2.getHeight();
		final int yResWhenCorrectlyRotated = bi.getWidth();
		Assert.assertEquals("Image orientation is incorrect. Height does not match expected value.", yResWhenCorrectlyRotated, yRes);
		
		return bi2;
	}
	
	@Test
	public void testImageOrientationOpenCv() throws IOException {
		logger.debug("Test to load an image with exif orientation set.");
		URL imgUrl = ImageTest.class.getClassLoader().getResource("img/exif_orientation_6.jpg");
		BufferedImage bi = ImageIO.read(imgUrl);
		Image img = new Image(imgUrl);
		Mat opencvImage = img.getImageOpenCVImage(true);
		
		int xRes = opencvImage.width();
		final int xResWhenCorrectlyRotated = bi.getHeight();
		Assert.assertEquals("Image orientation is incorrect. Width does not match expected value.", xResWhenCorrectlyRotated, xRes);
		
		int yRes = opencvImage.height();
		final int yResWhenCorrectlyRotated = bi.getWidth();
		Assert.assertEquals("Image orientation is incorrect. Height does not match expected value.", yResWhenCorrectlyRotated, yRes);
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
