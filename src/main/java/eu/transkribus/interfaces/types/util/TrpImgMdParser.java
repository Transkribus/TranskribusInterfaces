package eu.transkribus.interfaces.types.util;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;

import eu.transkribus.interfaces.util.URLUtils;

public class TrpImgMdParser {
	private static final Logger logger = LoggerFactory.getLogger(TrpImgMdParser.class);
	
	public final static int DEFAULT_EXIF_ORIENTATION = 1;

	private TrpImgMdParser() {}
	
	/**
	 * Inspect the image metadata for width, height and EXIF orientation tag value.
	 * The returned {@link ImageTransformation} object includes this data as well as the width and height of the image
	 * after rotating it according to the orienation tag value and an {@link AffineTransform} for doing so.
	 * 
	 * @param url
	 * @return
	 * @throws ImageProcessingException
	 * @throws IOException
	 * @throws MetadataException
	 */
	public static ImageTransformation readImageDimension(URL url) throws ImageProcessingException, IOException, MetadataException {
		long time = System.currentTimeMillis();
		try (InputStream is = URLUtils.getInputStream(url)) {
			ImageTransformation dim = readImageDimension(is);
			logger.debug("Exif orientation read from URL in " + (System.currentTimeMillis() - time) + " ms");
			return dim;
		}
	}

	/**
	 * Inspect the image metadata for width, height and EXIF orientation tag value.
	 * The returned {@link ImageTransformation} object includes this data as well as the width and height of the image
	 * after rotating it according to the orienation tag value and an {@link AffineTransform} for doing so.
	 * 
	 * @param url
	 * @return
	 * @throws ImageProcessingException
	 * @throws IOException
	 * @throws MetadataException
	 */
	public static ImageTransformation readImageDimension(File file)
			throws FileNotFoundException, IOException, ImageProcessingException, MetadataException {
		long time = System.currentTimeMillis();
		try (InputStream is = new FileInputStream(file)) {
			ImageTransformation dim = readImageDimension(is);
			logger.debug("Exif orientation read from File in " + (System.currentTimeMillis() - time) + " ms");
			return dim;
		}
	}

	/**
	 * Inspect the image metadata for width, height and EXIF orientation tag value.
	 * The returned {@link ImageTransformation} object includes this data as well as the width and height of the image
	 * after rotating it according to the orienation tag value and an {@link AffineTransform} for doing so.
	 * <br><br>
	 * This method searches specific EXIF directories only!! This might fail as e.g. Windows Photo Editor seems to push around tags into sub-dirs.
	 * See doc ID=5869 on test server. 
	 * 
	 * @param url
	 * @return
	 * @throws ImageProcessingException
	 * @throws IOException
	 * @throws MetadataException
	 */
	public static ImageTransformation readImageDimension(InputStream is)
			throws ImageProcessingException, IOException, MetadataException {
		long time = System.currentTimeMillis();
		Metadata metadata = ImageMetadataReader.readMetadata(is);
		
		
		//tags should be in ExifIFD0Directory or ExifSubIFDDirectory objects.
		//Windows Photo Editor 10.x seems to push tags into a sub-dir on edit. See doc ID=5869 on test server
		//That's why we have to check all EXIF directories here and not just the first ones.
		Collection<ExifDirectoryBase> dirs = metadata.getDirectoriesOfType(ExifDirectoryBase.class);
		
		if(logger.isTraceEnabled()) {
			for (Directory directory : metadata.getDirectories()) {
				logger.debug("Directory = " + directory.getName());
			    for (Tag tag : directory.getTags()) {
			    	logger.debug("" + tag);
			    }
			}
		}
		
		if(dirs.isEmpty()) {
			throw new MetadataException("No EXIF data found.");
		} else {
			int width = getExifTagValueInt(dirs, 0, ExifIFD0Directory.TAG_IMAGE_WIDTH, 
					ExifIFD0Directory.TAG_EXIF_IMAGE_WIDTH);
			int height = getExifTagValueInt(dirs, 0, ExifIFD0Directory.TAG_IMAGE_HEIGHT, 
					ExifIFD0Directory.TAG_EXIF_IMAGE_HEIGHT);
			final int orientation = getExifTagValueInt(dirs, DEFAULT_EXIF_ORIENTATION, 
					ExifIFD0Directory.TAG_ORIENTATION);
			logger.debug("Exif orientation read from stream in " + (System.currentTimeMillis() - time) + " ms");
			
			return getTransformation(new ImageDimension(orientation, width, height));
		}
	}
	
	/**
	 * Extract an int value from the exif directories.
	 * Accepts a list of tags to be searched for a value in the given order. First value found is returned.
	 * If none is found, defaultValue is returned.
	 * 
	 * @param dirs the exif directories to search in given order
	 * @param defaultValue returned if nothing is found
	 * @param tags a list of tags to be inspected in this order.
	 * @return
	 */
	private static int getExifTagValueInt(Collection<ExifDirectoryBase> dirs, int defaultValue, final int... tags) {
		for(ExifDirectoryBase dir : dirs) {
			for(int tag : tags) {
				if(dir.containsTag(tag)) {
					try {
						logger.debug("Found value in Exif directory '" + dir.getName() + "' for tag: " + tag + " = " + dir.getInt(tag));
						return dir.getInt(tag);
					} catch (MetadataException e) {
						logger.error("Could not extract Exif data value from directory '" + dir.getName() + "' for tag: " + tag, e);
					}
				} else {
					logger.debug("No value set in Exif directory '" + dir.getName() + "' for tag: " + tag);
				}
			}
		}
		return defaultValue;
	}
	
	/**
	 * Create an {@link AffineTransform} according the exif orientation int value included in dension
	 * 
	 * @param width
	 * @param height
	 * @param dimension {@link ImageDimension}
	 * @return
	 */
	public static ImageTransformation getTransformation(ImageDimension dimension) {
		AffineTransform t = new AffineTransform();
		int destWidth = dimension.getOriginalWidth();
		int destHeight = dimension.getOriginalHeight();
		switch (dimension.getExifOrientation()) {
		case 2: // Flip X
			t.scale(-1.0f, 1.0f);
			t.translate(-dimension.getOriginalWidth(), 0);
			break;
		case 3: // PI rotation
			t.translate(dimension.getOriginalWidth(), dimension.getOriginalHeight());
			t.rotate(toRadiant(180));
			break;
		case 4: // Flip Y
			t.scale(1.0f, -1.0f);
			t.translate(0, -dimension.getOriginalHeight());
			break;
		case 5: // - PI/2 and Flip X
			destWidth = dimension.getOriginalHeight();
			destHeight = dimension.getOriginalWidth();
			t.rotate(toRadiant(-90));
			t.scale(-1.0f, 1.0f);
			break;
		case 6: // -PI/2 and -width
			logger.debug("detected clockwise 90° rotation");
			destWidth = dimension.getOriginalHeight();
			destHeight = dimension.getOriginalWidth();
			t.translate(dimension.getOriginalHeight(), 0);
			t.rotate(toRadiant(90));
			break;
		case 7: // PI/2 and Flip
			destWidth = dimension.getOriginalHeight();
			destHeight = dimension.getOriginalWidth();
			t.scale(-1.0f, 1.0f);
			t.translate(-dimension.getOriginalHeight(), 0);
			t.translate(0, dimension.getOriginalWidth());
			t.rotate(toRadiant(270));
			break;
		case 8: // PI / 2
			destWidth = dimension.getOriginalHeight();
			destHeight = dimension.getOriginalWidth();
			t.translate(0, dimension.getOriginalWidth());
			t.rotate(toRadiant(270));
			break;
		default:
			// orientation "1" or anything else: do nothing
			break;
		}
		return new ImageTransformation(dimension, t, destWidth, destHeight);
	}
	
	/**
	 * Create an {@link AffineTransform} according the exif orientation int value given
	 * 
	 * @param width
	 * @param height
	 * @param exifOrientation an exifOrientation value. 1 is "normal", i.e. no transformation
	 * @return
	 */
	public static ImageTransformation getTransformation(int width, int height, int exifOrientation) {
		ImageDimension dim = new ImageDimension(exifOrientation, width, height);
		return getTransformation(dim);
	}
	
	private static double toRadiant(int degree) {
		return degree * Math.PI / 180;
	}
	
	/**
	 * Contains metadata included in an image that is needed for display and processing purposes.
	 *  
	 * @author philip
	 *
	 */
	private static class ImageDimension {
		private final int originalWidth;
		private final int originalHeight;
		private final int exifOrientation;
		private ImageDimension(final Integer exifOrientation, final int originalWidth, final int originalHeight) {
			this.originalWidth = originalWidth;
			this.originalHeight = originalHeight;
			this.exifOrientation = exifOrientation == null ? DEFAULT_EXIF_ORIENTATION : exifOrientation;
		}
		public int getOriginalWidth() {
			return originalWidth;
		}
		public int getOriginalHeight() {
			return originalHeight;
		}
		public int getExifOrientation() {
			return exifOrientation;
		}
		public boolean isDefaultOrientation() {
			return exifOrientation == DEFAULT_EXIF_ORIENTATION;
		}
	}
	/**
	 * Contains metadata read from an image that is needed for display and processing purposes, as well as 
	 * additional information needed for bringing the data in a usable form e.g. {@link AffineTransform}.
	 *  
	 * @author philip
	 *
	 */
	public static class ImageTransformation extends ImageDimension {
		private final AffineTransform transformation;
		private final int destinationWidth;
		private final int destinationHeight;
		private ImageTransformation(ImageDimension dimension, AffineTransform transformation, final int destinationWidth, final int destinationHeight) {
			super(dimension.getExifOrientation(), dimension.getOriginalWidth(), dimension.getOriginalHeight());
			this.transformation = transformation;
			this.destinationWidth = destinationWidth;
			this.destinationHeight = destinationHeight;
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
	}
}
