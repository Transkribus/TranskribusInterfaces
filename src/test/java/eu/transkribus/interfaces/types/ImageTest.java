package eu.transkribus.interfaces.types;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;

import eu.transkribus.interfaces.types.Image.Type;
import eu.transkribus.interfaces.types.util.ImageUtils;
import eu.transkribus.interfaces.types.util.TrpImageIO;
import eu.transkribus.interfaces.types.util.TrpImageIO.RotatedBufferedImage;
import eu.transkribus.interfaces.types.util.TrpImgMdParser;
import eu.transkribus.interfaces.types.util.TrpImgMdParser.ImageTransformation;

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
			logger.debug("Trying URL: " + url);
			Image img = new Image(url);
			img.createType(Type.OPEN_CV);

			logger.info("Created opencv image for url: " + url);
		}
	}
	
	@Test
	public void testFromUrlToBufferedImage() throws IOException {
		logger.debug("testFromUrlToBufferedImage");

		URL[] urls = { 
				new URL("https://dbis-thure.uibk.ac.at/f/Get?fileType=orig&id=VJCQMQBNZFDFCZMAJZHKNJKW"),
				new URL("http://www.austriatraveldirect.com/files/INNNORD01041.jpg"),
				//this URL will do a redirect to the HTTPS host
				new URL("http://dbis-thure.uibk.ac.at/f/Get?id=UNKRNHSATTZGUUMKZBSBNOUC"),
				new URL("https://files-test.transkribus.eu/Get?fileType=bin&id=CFELMFJLDLMWBFVUUDQTTMXR"),
				//this jpg will fail with "Invalid JPEG file structure: two SOF markers" https://github.com/haraldk/TwelveMonkeys/issues/197
//				new URL("file:/mnt/nmtera1/Content/fimagestore_trp/N/H/NHJCONKHIEQAHRGOBYVLFGOF/orig_Stadtratsprotokoll_1895-1898_0801.jpg")
				};

		for (URL url : urls) {
			logger.debug("Trying URL: " + url);
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
	
	
	@Test 
	public void testUnknownHost() throws MalformedURLException {
		URL imgUrl = new URL("https://files-test.transkribus.eu/Get?id=XBNTVRNUTFEDVFPPGIGIJRSN&fileType=view");
		URL invalidUrl = new URL("http://www.asdlakjdalskdjl.com");
		for(int i = 0; i < 50; i++) {
			try {
				TrpImgMdParser.readExifOrientationTag(imgUrl);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				Assert.fail("Could not read img md: " + e.getMessage());
			}
			try {
				TrpImgMdParser.readExifOrientationTag(invalidUrl);
				Assert.fail();
			} catch (Exception e) {
				logger.debug(e.getClass().getSimpleName() + ": " + e.getMessage());
			}
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
		//Use standard ImageIO as reference
		BufferedImage bi = ImageIO.read(imgUrl);
		Image img = new Image(imgUrl);
		BufferedImage bi2 = img.getImageBufferedImage(true);
		
		int xRes = bi2.getWidth();
		//xRes is original height for that image
		final int xResWhenCorrectlyRotated = bi.getHeight();
		Assert.assertEquals("Image orientation is incorrect. Width does not match expected value.", xResWhenCorrectlyRotated, xRes);
		
		int yRes = bi2.getHeight();
		//yRes is original Width
		final int yResWhenCorrectlyRotated = bi.getWidth();
		Assert.assertEquals("Image orientation is incorrect. Height does not match expected value.", yResWhenCorrectlyRotated, yRes);
		
		return bi2;
	}
	
	@Test
	public void testImageOrientationOpenCv() throws IOException {
		logger.debug("Test to load an image with exif orientation set.");
		URL imgUrl = new URL("https://files-test.transkribus.eu/Get?id=XBNTVRNUTFEDVFPPGIGIJRSN&fileType=view");
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
	 * Image taken from doc ID=5869 on test server.
	 * It seems it was edited with Windows Photo Editor 10.x which has pushed tags into a sub-dir on edit. Updated method should handle this.
	 * 
	 * @throws IOException
	 * @throws ImageProcessingException
	 * @throws MetadataException
	 */
	@Test
	public void testImageFromWindowsPhotoEditor() throws IOException, ImageProcessingException, MetadataException {
		URL url = new URL("https://files-test.transkribus.eu/Get?id=JMWVACVZDPHVPOVGFWQVBHXP");
		RotatedBufferedImage bi = (RotatedBufferedImage) TrpImageIO.read(url);
		logger.info("TrpImageIO result = " + bi.getImageTransformation().toString());
		
		ImageTransformation t = TrpImgMdParser.readImageDimension(url);
		logger.info("TrpImgMdParser result = " + t.toString());
	}
	
	
	
	/**
	 * Image taken from doc ID=5869 on test server.
	 * It seems it was edited with Windows Photo Editor 10.x which has pushed tags into a sub-dir on edit. Updated method should handle this.
	 * 
	 * @throws IOException
	 * @throws ImageProcessingException
	 * @throws MetadataException
	 */
	@Test
	public void testImageFromWindowsPhotoEditor2() throws IOException, ImageProcessingException, MetadataException {
		URL url = new URL("https://dbis-thure.uibk.ac.at/f/Get?id=BTCMSAGPZYLKPPLZWWQLSMKY");
		RotatedBufferedImage bi = (RotatedBufferedImage) TrpImageIO.read(url);
		logger.info("TrpImageIO result = " + bi.getImageTransformation().toString());
		
		ImageTransformation t = TrpImgMdParser.readImageDimension(url);
		logger.info("TrpImgMdParser result = " + t.toString());
	}
	
	
	/**
	 * This image was taken with a Samsung smartphone and edited in Paint.net photo editor.
	 * TrpImgMdParser erroneously respected the orientation tag from the thumbnail section in the exif data and rotated the image falsely.
	 */
	@Test
	public void testImageFromPaintNet() throws IOException, URISyntaxException {
		final String filePath = "img_orientation/view_20170929_124415.jpg";
		URL url = this.getClass().getClassLoader().getResource(filePath);
		File imgFile = new File(url.toURI());
		logger.debug("File '{}' exists = {}", filePath, imgFile.isFile());
		BufferedImage bi = TrpImageIO.read(imgFile);
		
		if(bi instanceof RotatedBufferedImage) {
			//if we are here this is broken
			Assert.fail("Image was erroneously rotated!");
		}
	}
	
	@Test
	public void testSSLHandshake() throws IOException {
		 URL url = new URL("https://dbis-thure.uibk.ac.at/f/Get?id=YFUDYKUQSUYVGAIFQOAAYEPW&fileType=view");
		 try(InputStream is = url.openStream();) {
			 //success
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
