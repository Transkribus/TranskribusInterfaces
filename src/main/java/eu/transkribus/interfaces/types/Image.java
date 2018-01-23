/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces.types;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;

import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi;

import eu.transkribus.interfaces.types.util.ImageUtils;

/**
 *
 * @author gundram
 */
public class Image {
	private static final Logger logger = LoggerFactory.getLogger(Image.class);

	private URL imageUrl;
	private BufferedImage imageBufferedImage;
	private Mat imageOpenCVImage;

	public enum Depth {
		BYTE, BINARY
	}

	public enum Type {
		URL, OPEN_CV, JAVA
	}

	/**
	 * This type already exists as instance in C++ <br/>
	 * In C++ and Java there have to be methods to use such an image. The
	 * position in the memory is accessible using
	 * this.imageOpenCVImage.nativeObject
	 *
	 * @param image
	 */
	public Image(Mat image) {
		this.imageOpenCVImage = image;
	}

	/**
	 * If the image should not be transfered by this instance, it is possible
	 * that the image is loaded by the URL
	 *
	 * @param url
	 */
	public Image(URL url) {
		this.imageUrl = url;
	}

	/**
	 * as first workaround - maybe deleted later
	 *
	 * @param imageBufferedImage
	 */
	public Image(BufferedImage imageBufferedImage) {
		this.imageBufferedImage = imageBufferedImage;
	}
	
	public Object getImage(Type type, boolean createIfNecessary) {
		Object o = getImageObject(type);
		if (o == null && createIfNecessary) {
			try {
				createType(type);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			return getImageObject(type);
		} else {
			return o;
		}
	}
	
	public Mat getImageOpenCVImage() {
		return getImageOpenCVImage(false);
	}
	
	public Mat getImageOpenCVImage(boolean createIfNecessary) {
		return (Mat) getImage(Type.OPEN_CV, createIfNecessary);
	}
	
	public BufferedImage getImageBufferedImage() {
		return getImageBufferedImage(false);
	}

	public BufferedImage getImageBufferedImage(boolean createIfNecessary) {
		return (BufferedImage) getImage(Type.JAVA, createIfNecessary);
	}
	
	public URL getImageUrl() {
		return getImageUrl(false);
	}

	public URL getImageUrl(boolean createIfNecessary) {
		return (URL) getImage(Type.URL, createIfNecessary);
	}
	
	public Set<Type> getAvailableTypes() {
		Set<Type> types = new HashSet<Type>();
		
		for (Type t : Type.values()) {
			if (hasType(t))
				types.add(t);
		}
		
		return types;		
	}
	
	public String getAvailableTypesString() {
		String s="";
		for (Type t : getAvailableTypes()) {
			s += t.name()+" ";
		}
		return s.trim();
	}

	/**
	 * shows if a given type is supported
	 *
	 * @param type
	 * @return
	 */
	public boolean hasType(Type type) {
		switch (type) {
		case JAVA:
			return imageBufferedImage != null;
		case OPEN_CV:
			return imageOpenCVImage != null;
		case URL:
			return imageUrl != null;
		default:
			throw new RuntimeException("unknown type '" + type + "'");
		}
	}

	/**
	 * create internal repesentation for the given type, if possible.
	 *
	 * @param toType
	 * @throws IOException
	 */
	public void createType(Type toType) throws IOException {
		switch (toType) {
		case JAVA:
			if (this.hasType(Type.JAVA)) {
				break;
			} else if (this.hasType(Type.OPEN_CV)) {
				imageBufferedImage = ImageUtils.convertToBufferedImage(imageOpenCVImage);
			} else if (this.hasType(Type.URL)) {
				imageBufferedImage = ImageUtils.convertToBufferedImage(imageUrl);
			}
			break;
		case OPEN_CV:
			if (this.hasType(Type.OPEN_CV)) {
				break;
			} else if (this.hasType(Type.URL)) {
				imageOpenCVImage = ImageUtils.convertToOpenCvImage(imageUrl);
			} else if(this.hasType(Type.JAVA)){
				imageOpenCVImage = ImageUtils.convertToOpenCvImage(imageBufferedImage);
			}
			break;
		case URL:
			throw new IOException("cannot convert from instance to url");
		default:
			throw new RuntimeException("unknown type '" + toType + "'");
		}
	}
	
	private Object getImageObject(Type type) {
		switch (type) {
		case URL:
			return imageUrl;
		case OPEN_CV:
			return imageOpenCVImage;
		case JAVA:
			return imageBufferedImage;
		default:
			throw new RuntimeException("unknown type '" + type + "'");
		}
	}
	
	static {
		registerImageIOServices();
		listImageIOServices();
	}
	
	public static void listImageIOServices() {
		IIORegistry registry = IIORegistry.getDefaultInstance();
		logger.debug("image-io services:");
		Iterator<Class<?>> cats = registry.getCategories();
		while (cats.hasNext()) {
			Class<?> cat = cats.next();
			logger.debug("image-io category = " + cat);

			Iterator<?> providers = registry.getServiceProviders(cat, true);
			while (providers.hasNext()) {
				logger.debug("image-io provider = " + providers.next());
			}
		}
	}
	
	public static void registerImageIOServices() {
		logger.debug("registering image readers / writers");
		
		IIORegistry registry = IIORegistry.getDefaultInstance();

		// have to programmatically register tiff reader / writer in tomcat
		registry.registerServiceProvider(new TIFFImageWriterSpi(), javax.imageio.spi.ImageWriterSpi.class);
		registry.registerServiceProvider(new TIFFImageReaderSpi(), javax.imageio.spi.ImageReaderSpi.class);

		registry.registerServiceProvider(new com.twelvemonkeys.imageio.plugins.tiff.TIFFImageWriterSpi(),
				javax.imageio.spi.ImageWriterSpi.class);
		registry.registerServiceProvider(new com.twelvemonkeys.imageio.plugins.tiff.TIFFImageReaderSpi(),
				javax.imageio.spi.ImageReaderSpi.class);
		
	}

	public static void testReaders() throws IOException {
		String[] formats = { "JPEG", "TIFF", "TIF", "PNG" };
		for (String format : formats) {
			logger.info("testing readers for format: " + format);
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(format);

			if (!readers.hasNext()) {
				throw new IOException("No reader found for format: " + format);
			}

			while (readers.hasNext()) {
				logger.info("reader: " + readers.next());
			}
		}
	}
	
	public void dispose() {
		if(imageBufferedImage != null) {
			imageBufferedImage.flush();
		}
		if(imageOpenCVImage != null) {
			imageOpenCVImage.release();
		}
	}
	
	public static void main(String[] args) throws IOException {
		testReaders();
	}

}
