/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.transkribus.interfaces.types;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import eu.transkribus.interfaces.types.util.ImageConvertUtils;

/**
 *
 * @author gundram
 */
public class Image {

	private URL imageUrl;
	private BufferedImage imageBufferedImage;
	private Mat imageOpenCVImage;

	// private Polygon polygon;
	// private String[] properties;
	private Type type;
	private Depth depth;

	public enum Depth {
		BYTE, BINARY
	}

	public enum Type {
		URL, OPEN_CV, JAVA
	}

	public Depth getDepth() {
		return this.depth;
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
		type = Type.OPEN_CV;
	}

	/**
	 * If the image should not be transfered by this instance, it is possible
	 * that the image is loaded by the URL
	 *
	 * @param url
	 */
	public Image(URL url) {
		this.imageUrl = url;
		type = Type.URL;
	}

	/**
	 * as first workaround - maybe deleted later
	 *
	 * @param imageBufferedImage
	 */
	public Image(BufferedImage imageBufferedImage) {
		this.imageBufferedImage = imageBufferedImage;
		type = Type.JAVA;
	}

	/**
	 * as first workaround
	 *
	 * @return
	 */
	public Mat getImageOpenCVImage() {
		if (imageOpenCVImage == null) {
			throw new RuntimeException(
					"no convertion done from " + type.toString() + " to " + Type.OPEN_CV + " implemented.");
		}
		return imageOpenCVImage;
	}

	/**
	 * as first workaround
	 *
	 * @return
	 */
	public BufferedImage getImageBufferedImage() {
		if (imageBufferedImage == null) {
			throw new RuntimeException(
					"no convertion done from " + type.toString() + " to " + Type.JAVA + " implemented.");
		}
		return imageBufferedImage;
	}

	public URL getImageUrl() {
		if (type != Type.URL) {
			throw new RuntimeException("no url given, image was set with type " + type.toString() + ".");
		}
		return imageUrl;
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
	public void convert(Type toType) throws IOException {
		switch (toType) {
		case JAVA:
			if (this.hasType(Type.JAVA)) {
				break;
			} else if (this.hasType(Type.OPEN_CV)) {
				imageBufferedImage = ImageConvertUtils.convertToBufferedImage(imageOpenCVImage);
			} else if (this.hasType(Type.URL)) {
				imageBufferedImage = ImageIO.read(imageUrl);
			}
			break;
		case OPEN_CV:
			if (this.hasType(Type.OPEN_CV)) {
				break;
			} else if (this.hasType(Type.URL)) {
				File imgFile = ImageConvertUtils.downloadImgFile(imageUrl);
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				imageOpenCVImage = Highgui.imread(imgFile.getAbsolutePath());
			} else if(this.hasType(Type.JAVA)){
				//TODO
			}
			break;
		case URL:
			throw new IOException("cannot convert from instance to url");
		default:
			throw new RuntimeException("unknown type '" + toType + "'");
		}
	}

}
