package eu.transkribus.interfaces.types;

import java.io.IOException;
import java.net.URL;

import eu.transkribus.interfaces.types.Image.Type;
import eu.transkribus.interfaces.types.util.ImageUtils;

public class ImageTest {

	public static void main(String[] args) throws IOException {
		
		URL[] urls = {
				new URL("http://www.austriatraveldirect.com/files/INNNORD01041.jpg"),
				new URL("http://dbis-thure.uibk.ac.at/f/Get?id=UNKRNHSATTZGUUMKZBSBNOUC")
		};
		
		for(int i = 0; i < urls.length; i++) {
			URL u = urls[i];
			Image urlImage = new Image(u);
			try {
//				urlImage.convert(Type.JAVA);
				
				urlImage.createType(Type.OPEN_CV);
				
				urlImage.createType(Type.JAVA);
				
//				ImageUtils.saveAsFile(urlImage.getImageBufferedImage(), "/tmp/test-" + i + "-java.jpg");
//				ImageUtils.saveAsFile(urlImage.getImageOpenCVImage(), "/tmp/test-" + i + "-mat.jpg");
				
			} catch (Throwable e){
				System.out.println("Error on file: " + u.toString());
				e.printStackTrace();
			}
		}
	}

}
