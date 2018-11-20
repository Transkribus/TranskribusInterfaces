package eu.transkribus.interfaces.types.util;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;

public class TrpImgMdParser {
	private static final Logger logger = LoggerFactory.getLogger(TrpImgMdParser.class);
	
	public final static int DEFAULT_EXIF_ORIENTATION = 1;

	private TrpImgMdParser() {}
	
	public static ImageDimension readImageDimension(URL url) throws ImageProcessingException, IOException, MetadataException {
		long time = System.currentTimeMillis();
		try (InputStream is = url.openStream()) {
			ImageDimension dim = readImageDimension(is);
			logger.debug("Exif orientation read from URL in " + (System.currentTimeMillis() - time) + " ms");
			return dim;
		}
	}

	public static ImageDimension readImageDimension(File file)
			throws FileNotFoundException, IOException, ImageProcessingException, MetadataException {
		long time = System.currentTimeMillis();
		try (InputStream is = new FileInputStream(file)) {
			ImageDimension dim = readImageDimension(is);
			logger.debug("Exif orientation read from File in " + (System.currentTimeMillis() - time) + " ms");
			return dim;
		}
	}

	public static ImageDimension readImageDimension(InputStream is)
			throws ImageProcessingException, IOException, MetadataException {
		long time = System.currentTimeMillis();
		Metadata metadata = ImageMetadataReader.readMetadata(is);
		ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
		if(logger.isTraceEnabled()) {
			for (Directory directory : metadata.getDirectories()) {
				logger.trace("Directory = " + directory.getName());
			    for (Tag tag : directory.getTags()) {
			    	logger.trace("" + tag);
			    }
			}
		}
		if(exifIFD0Directory == null) {
			throw new MetadataException("No EXIF data found.");
		} else {
			int width = getExifTagValueInt(exifIFD0Directory, 0, ExifIFD0Directory.TAG_IMAGE_WIDTH, 
					ExifIFD0Directory.TAG_EXIF_IMAGE_WIDTH);
			int height = getExifTagValueInt(exifIFD0Directory, 0, ExifIFD0Directory.TAG_IMAGE_HEIGHT, 
					ExifIFD0Directory.TAG_EXIF_IMAGE_HEIGHT);
			final int orientation = getExifTagValueInt(exifIFD0Directory, DEFAULT_EXIF_ORIENTATION, 
					ExifIFD0Directory.TAG_ORIENTATION);
			logger.debug("Exif orientation read from stream in " + (System.currentTimeMillis() - time) + " ms");
			return getTransformation(width, height, orientation);
		}
	}

	/**
	 * Extract an int value from the exif directory.
	 * Accepts a list of tags to be searched for a value in the given order. First value found is returned.
	 * If none is found, defaultValue is returned.
	 * 
	 * @param dir
	 * @param defaultValue returned if nothing is found
	 * @param tags a list of tags to be inspected in this order.
	 * @return
	 */
	private static int getExifTagValueInt(ExifIFD0Directory dir, int defaultValue, final int... tags) {
		for(int tag : tags) {
			if(dir.containsTag(tag)) {
				try {
					return dir.getInt(tag);
				} catch (MetadataException e) {
					logger.error("Could not extract Exif data value for tag: " + tag, e);
				}
			} else {
				logger.debug("No value set in Exif data for tag: " + tag);
			}
		}
		return defaultValue;
	}
	
	/**
	 * Create an {@link AffineTransform} according the exif orientation int value given
	 * 
	 * @param width
	 * @param height
	 * @param exifOrientation an exifOrientation value. 1 is "normal", i.e. no transformation
	 * @return
	 */
	public static ImageDimension getTransformation(int width, int height, int exifOrientation) {
		AffineTransform t = new AffineTransform();
		int destWidth = width;
		int destHeight = height;
		switch (exifOrientation) {
		case 2: // Flip X
			t.scale(-1.0f, 1.0f);
			t.translate(-width, 0);
			break;
		case 3: // PI rotation
			t.translate(width, height);
			t.rotate(toRadiant(180));
			break;
		case 4: // Flip Y
			t.scale(1.0f, -1.0f);
			t.translate(0, -height);
			break;
		case 5: // - PI/2 and Flip X
			destWidth = height;
			destHeight = width;
			t.rotate(toRadiant(-90));
			t.scale(-1.0f, 1.0f);
			break;
		case 6: // -PI/2 and -width
			logger.debug("detected clockwise 90° rotation");
			destWidth = height;
			destHeight = width;
			t.translate(height, 0);
			t.rotate(toRadiant(90));
			break;
		case 7: // PI/2 and Flip
			destWidth = height;
			destHeight = width;
			t.scale(-1.0f, 1.0f);
			t.translate(-height, 0);
			t.translate(0, width);
			t.rotate(toRadiant(270));
			break;
		case 8: // PI / 2
			destWidth = height;
			destHeight = width;
			t.translate(0, width);
			t.rotate(toRadiant(270));
			break;
		default:
			// orientation "1" or anything else: do nothing
			break;
		}
		return new ImageDimension(t, exifOrientation, destWidth, destHeight);
	}
	
	private static double toRadiant(int degree) {
		return degree * Math.PI / 180;
	}
	
	/**
	 * Contains metadata for an image that is necessary for displaying and processing it correctly, 
	 * i.e. the width, height and an {@link AffineTransform} in case the image data has to be altered to bring 
	 * it into the correct form, matching the given width and height values.
	 *  
	 * @author philip
	 *
	 */
	public static class ImageDimension {
		private final AffineTransform transformation;
		private final int destinationWidth;
		private final int destinationHeight;
		private final int exifOrientation;
		private ImageDimension(AffineTransform transformation, final Integer exifOrientation, final int destinationWidth, final int destinationHeight) {
			this.transformation = transformation;
			this.destinationWidth = destinationWidth;
			this.destinationHeight = destinationHeight;
			this.exifOrientation = exifOrientation == null ? DEFAULT_EXIF_ORIENTATION : exifOrientation;
		}
		public AffineTransform getTransformation() {
			return transformation;
		}
		public int getDestinationWidth() {
			return destinationWidth;
		}
		public int getDestinationHeight() {
			return destinationHeight;
		}
		public int getExifOrientation() {
			return exifOrientation;
		}
	}
}
