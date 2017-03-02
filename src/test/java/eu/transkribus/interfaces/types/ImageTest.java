package eu.transkribus.interfaces.types;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.log4j.Logger;
import org.junit.Test;

import eu.transkribus.interfaces.types.Image.Type;

public class ImageTest {
	
	private static final Logger logger = Logger.getLogger(ImageTest.class);
	
//	public static void main(String[] args) {
//        System.out.println(PackageUtil.getVendor());
//        System.out.println(PackageUtil.getVersion());
//        System.out.println(PackageUtil.getSpecificationTitle());
//    }
	
	@Test
	public void testReaders() throws IOException {
		
		Image.testReaders();
		
		
	}
	
	@Test
	public void testFromUrlToOpenCv() throws IOException {
		logger.debug("testFromUrlToOpenCv");
		
		URL[] urls = {
				new URL("http://www.austriatraveldirect.com/files/INNNORD01041.jpg"),
				new URL("http://dbis-thure.uibk.ac.at/f/Get?id=UNKRNHSATTZGUUMKZBSBNOUC"),
				new URL("https://dbis-thure.uibk.ac.at/f/Get?fileType=bin&id=CFELMFJLDLMWBFVUUDQTTMXR")
		};
		
		for (URL url : urls) {
			Image img = new Image(url);
			img.createType(Type.OPEN_CV);
			
			logger.info("Created opencv image for url: "+url);
		}

	}
	
}
