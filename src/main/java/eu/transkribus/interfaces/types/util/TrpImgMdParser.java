package eu.transkribus.interfaces.types.util;

import java.awt.Dimension;
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
import com.drew.metadata.exif.ExifThumbnailDirectory;

import eu.transkribus.interfaces.util.URLUtils;

public class TrpImgMdParser {
	private static final Logger logger = LoggerFactory.getLogger(TrpImgMdParser.class);
	
	public final static int DEFAULT_EXIF_ORIENTATION = 1;
	
	private static final int UNDEFINED_DIM_VALUE = -1;

	private TrpImgMdParser() {}
	
	/**
	 * Array with Exif directory names (as defined in the metadata-extractor) to be ignored when reading image metadata regarding the main image
	 */
	private static final String[] EXIF_DIR_NAME_BLACKLIST = { 
			new ExifThumbnailDirectory().getName()
		};
	

	/**
	 * Inspect the image metadata for an EXIF orientation tag value.
	 * 
	 * @param url
	 * @return
	 * @throws ImageProcessingException An exception class thrown upon an unexpected condition that was fatal for the processing of an image.
	 * @throws IOException in case of an IO problem
	 */
	public static int readExifOrientationTag(URL url) throws ImageProcessingException, IOException {
		long time = System.currentTimeMillis();
		try (InputStream is = URLUtils.getInputStream(url)) {
			int orientation = readExifOrientationTag(is);
			logger.debug("Exif orientation read from URL in " + (System.currentTimeMillis() - time) + " ms");
			return orientation;
		}
	}

	/**
	 * Inspect the image metadata for an EXIF orientation tag value.
	 * 
	 * @param url
	 * @return
	 * @throws ImageProcessingException An exception class thrown upon an unexpected condition that was fatal for the processing of an image.
	 * @throws IOException in case of an IO problem
	 */
	public static int readExifOrientationTag(File file)
			throws FileNotFoundException, IOException, ImageProcessingException {
		long time = System.currentTimeMillis();
		try (InputStream is = new FileInputStream(file)) {
			int orientation = readExifOrientationTag(is);
			logger.debug("Exif orientation read from File in " + (System.currentTimeMillis() - time) + " ms");
			return orientation;
		}
	}

	
	/**
	 * Inspect the image metadata for an EXIF orientation tag value.
	 * 
	 * @param url
	 * @return
	 * @throws ImageProcessingException An exception class thrown upon an unexpected condition that was fatal for the processing of an image.
	 * @throws IOException in case of an IO problem
	 */
	public static int readExifOrientationTag(InputStream is) throws ImageProcessingException, IOException {
		long time = System.currentTimeMillis();
		Metadata metadata = ImageMetadataReader.readMetadata(is);
		Collection<ExifDirectoryBase> dirs = metadata.getDirectoriesOfType(ExifDirectoryBase.class);
		
		if(logger.isTraceEnabled()) {
			logMetadataContent(metadata);
		}
		
		if(dirs.isEmpty()) {
			return DEFAULT_EXIF_ORIENTATION;
		} else {
			final int orientation = getExifTagValueInt(dirs, DEFAULT_EXIF_ORIENTATION, 
					ExifIFD0Directory.TAG_ORIENTATION);
			logger.debug("Exif orientation read from stream in " + (System.currentTimeMillis() - time) + " ms");
			logger.debug("orientation = " + orientation);
			return orientation;
		}
	}

	/**
	 * Inspect the image metadata for width, height and EXIF orientation tag value.
	 * The returned {@link ImageTransformation} object includes this data as well as the width and height of the image
	 * after rotating it according to the orientation tag value and an {@link AffineTransform} for doing so.
	 * <br><br>
	 * FIXME: this method may return incomplete data as width and height may be located in different metadata 
	 * directories (see https://github.com/drewnoakes/metadata-extractor/issues/10).
	 * 
	 * 
	 * @param url
	 * @return
	 * @throws ImageProcessingException An exception class thrown upon an unexpected condition that was fatal for the processing of an image.
	 * @throws IOException in case of an IO problem
	 * @throws MetadataException no embedded image metadata was found
	 */
	public static ImageTransformation readImageDimension(URL url) throws ImageProcessingException, IOException, MetadataException {
		long time = System.currentTimeMillis();
		try (InputStream is = URLUtils.getInputStream(url)) {
			ImageDimension dim = readImageDimension(is);
			logger.debug("Exif orientation read from URL in " + (System.currentTimeMillis() - time) + " ms");
			if(dim.getOriginalWidth() == UNDEFINED_DIM_VALUE || dim.getOriginalHeight() == UNDEFINED_DIM_VALUE) {
				logger.warn("EXIF data is incomplete. Inspecting image data for dimension.");
				Dimension dimFromImgData = TrpImageIO.readImageDimensions(url);
				dim = new ImageDimension(dim.getExifOrientation(), dimFromImgData);
			}
			return getTransformation(dim);
		}
	}


	/**
	 * Inspect the image metadata for width, height and EXIF orientation tag value.
	 * The returned {@link ImageTransformation} object includes this data as well as the width and height of the image
	 * after rotating it according to the orientation tag value and an {@link AffineTransform} for doing so.
	 * <br><br>
	 * FIXME: this method may return incomplete data as width and height may be located in different metadata 
	 * directories (see https://github.com/drewnoakes/metadata-extractor/issues/10).
	 * 
	 * @param url
	 * @return
	 * @throws ImageProcessingException An exception class thrown upon an unexpected condition that was fatal for the processing of an image.
	 * @throws IOException in case of an IO problem
	 * @throws MetadataException no embedded image metadata was found
	 */
	public static ImageTransformation readImageDimension(File file)
			throws FileNotFoundException, IOException, ImageProcessingException, MetadataException {
		long time = System.currentTimeMillis();
		try (InputStream is = new FileInputStream(file)) {
			ImageDimension dim = readImageDimension(is);
			logger.debug("Exif orientation read from File in " + (System.currentTimeMillis() - time) + " ms");
			if(dim.getOriginalWidth() == UNDEFINED_DIM_VALUE || dim.getOriginalHeight() == UNDEFINED_DIM_VALUE) {
				logger.warn("EXIF data is incomplete. Inspecting image data for dimension.");
				Dimension dimFromImgData = TrpImageIO.readImageDimensions(file);
				dim = new ImageDimension(dim.getExifOrientation(), dimFromImgData);
			}
			return getTransformation(dim);
		}
	}

	/**
	 * Inspect the image metadata for width, height and EXIF orientation tag value.
	 * The returned {@link ImageTransformation} object includes this data as well as the width and height of the image
	 * after rotating it according to the orientation tag value and an {@link AffineTransform} for doing so.
	 * <br><br>
	 * This method searches specific EXIF directories only!! This might fail as e.g. Windows Photo Editor seems to push around tags into sub-dirs.
	 * See doc ID=5869 on test server.
	 * The result of this method always has to be checked for completeness regarding width and height, which is why it's private and the 
	 * methods using it do this.
	 * 
	 * 
	 * @param url
	 * @return
	 * @throws ImageProcessingException
	 * @throws IOException
	 * @throws MetadataException
	 */
	private static ImageDimension readImageDimension(InputStream is)
			throws ImageProcessingException, IOException, MetadataException {
		long time = System.currentTimeMillis();
		Metadata metadata = ImageMetadataReader.readMetadata(is);
		
		//tags should be in ExifIFD0Directory or ExifSubIFDDirectory objects.
		//Windows Photo Editor 10.x seems to push tags into a sub-dir on edit. See e.g. doc ID=5869 on test server
		//That's why we have to check all EXIF directories here and not just the first ones.
		Collection<ExifDirectoryBase> dirs = metadata.getDirectoriesOfType(ExifDirectoryBase.class);
		
		if(logger.isTraceEnabled()) {
			logMetadataContent(metadata);
		}
		
		if(dirs.isEmpty()) {
			throw new MetadataException("No EXIF data found.");
		} else {
			int width = getExifTagValueInt(dirs, UNDEFINED_DIM_VALUE, ExifIFD0Directory.TAG_IMAGE_WIDTH, 
					ExifIFD0Directory.TAG_EXIF_IMAGE_WIDTH);
			int height = getExifTagValueInt(dirs, UNDEFINED_DIM_VALUE, ExifIFD0Directory.TAG_IMAGE_HEIGHT, 
					ExifIFD0Directory.TAG_EXIF_IMAGE_HEIGHT);
			final int orientation = getExifTagValueInt(dirs, DEFAULT_EXIF_ORIENTATION, 
					ExifIFD0Directory.TAG_ORIENTATION);
			logger.debug("Exif orientation read from stream in " + (System.currentTimeMillis() - time) + " ms");
			logger.debug("width = " + width + " | height = " + height + " | orientation = " + orientation);
			
			/**
			 * if width/height are now UNDEFINED_DIM_VALUE (as those values are included in non-exif directory)
			 * we would have to check the filetype in FileTypeDirectory and check the directories for the specific format.
			 * 
			 * Calling methods have to fill up missing data by inspecting the image data.
			 */
			return new ImageDimension(orientation, width, height);
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
			if(isIgnoredExifDirectory(dir)) {
				continue;
			}
			for(int tag : tags) {
				if(dir.containsTag(tag)) {
					try {
						logger.debug("Found value in Exif directory {} '{}' for tag: {} = {}", 
								dir.getClass().getSimpleName(), dir.getName(), tag, dir.getInt(tag));
						return dir.getInt(tag);
					} catch (MetadataException e) {
						logger.error("Could not extract Exif data value from directory '" + dir.getName() + "' for tag: " + tag, e);
					}
				} else {
					logger.debug("No value set in Exif directory '{}' for tag: {}",  dir.getName(), tag);
				}
			}
		}
		return defaultValue;
	}
	
	/**
	 * Test if the ExifDirectory is to be ignored when handling the main image. E.g. we do not want to respect data in the {@link ExifThumbnailDirectory}.<br>
	 * Dir names to be ignored are defined by {@link #EXIF_DIR_NAME_BLACKLIST}
	 * @return true if the directory name is blacklisted
	 */
	private static boolean isIgnoredExifDirectory(ExifDirectoryBase dir) {
		for(String ignoredDirName : EXIF_DIR_NAME_BLACKLIST) {
			if(ignoredDirName.equals(dir.getName())) {
				logger.debug("Ignoring Exif directory: {}", dir.getName());
				return true;
			}
		}
		return false;
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
	
	private static void logMetadataContent(Metadata metadata) {
		for (Directory directory : metadata.getDirectories()) {
			logger.debug("Directory = " + directory.getName());
		    for (Tag tag : directory.getTags()) {
		    	logger.debug(tag + " (" + tag.getDirectoryName() + " -> " + tag.getTagType() + ")");
		    }
		}
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
		private ImageDimension(final Integer exifOrientation, final Dimension dim) {
			this.originalWidth = Double.valueOf(Math.floor(dim.getWidth())).intValue();
			this.originalHeight = Double.valueOf(Math.floor(dim.getHeight())).intValue();
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
