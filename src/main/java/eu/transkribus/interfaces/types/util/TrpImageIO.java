package eu.transkribus.interfaces.types.util;

import java.awt.Dimension;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageReaderWriterSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;

import eu.transkribus.interfaces.types.util.TrpImgMdParser.ImageTransformation;

/**
 * Methods taken from ImageIO and adapted to respect the image EXIF metadata's
 * orientation tag.
 * Returned BufferedImage objects are already rotated according to that data.
 *
 */
public class TrpImageIO {
	private static final Logger logger = LoggerFactory.getLogger(TrpImageIO.class);

	private static List<ImageReaderSpi> iioReaderList = new ArrayList<>();
	private static List<ImageWriterSpi> iioWriterList = new ArrayList<>();

	// Store instances of plugins to be registered AND unregistered. Not doing this
	// will leave a mess in IIORegistry/Tomcat after hot (un)deployment
	static {
		// tiff writers
//		iioWriterList.add(new com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi());
		iioWriterList.add(new com.twelvemonkeys.imageio.plugins.tiff.TIFFImageWriterSpi());
		// tiff readers
//		iioReaderList.add(new com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi());
		iioReaderList.add(new com.twelvemonkeys.imageio.plugins.tiff.TIFFImageReaderSpi());
		
		iioReaderList.add(new org.apache.pdfbox.jbig2.JBIG2ImageReaderSpi());
	}

	private TrpImageIO() {}

	/**
	 * See {@link ImageIO#read(File)}<br>
	 * This method additionally takes into account the exif orientation and rotates
	 * the image accordingly. If an image can not be read it throws an IOException
	 * instead of returning null.
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage read(File input) throws IOException {
		if (input == null) {
			throw new IllegalArgumentException("input == null!");
		}
		if (!input.canRead()) {
			throw new IIOException("Can't read input file!");
		}
		// read orientation data
		ImageTransformation dim = null;
		try {
			dim = TrpImgMdParser.readImageDimension(input);
		} catch (ImageProcessingException | MetadataException e) {
			logger.error("Could not extract metadata from file: " + input.getAbsolutePath(), e);
		}
		ImageInputStream stream = ImageIO.createImageInputStream(input);
		if (stream == null) {
			throw new IIOException("Can't create an ImageInputStream!");
		}
		BufferedImage bi = ImageIO.read(stream);
		if (bi == null) {
			stream.close();
			throw new IOException("Could not read image file: " + input.getName());
		}
		// fix orientation
		return transformImage(bi, dim);
	}

	/**
	 * See {@link ImageIO#read(URL)} This method additionally takes into account the
	 * exif orientation and rotates the image accordingly. If an image can not be
	 * read it throws an IOException instead of returning null.
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage read(URL input) throws IOException {
		if (input == null) {
			throw new IllegalArgumentException("input == null!");
		}

		InputStream istream = null;
		try {
			istream = input.openStream();
		} catch (IOException e) {
			throw new IIOException("Can't get input stream from URL!", e);
		}
		// read orientation data
		ImageTransformation dim = null;
		try {
			dim = TrpImgMdParser.readImageDimension(input);
		} catch (ImageProcessingException | MetadataException e) {
			logger.error("Could not extract metadata from file: " + input, e);
		}
		ImageInputStream stream = ImageIO.createImageInputStream(istream);
		BufferedImage bi;
		try {
			bi = ImageIO.read(stream);
			if (bi == null) {
				stream.close();
				throw new IOException("Could not read image from URL: " + input);
			}
		} finally {
			istream.close();
		}
		return transformImage(bi, dim);
	}

	public static BufferedImage read(byte[] d) throws IOException {
		try (InputStream input = new ByteArrayInputStream(d)) {
			return read(input);
		}
	}

	/**
	 * See {@link ImageIO#read(InputStream)} This method additionally takes into
	 * account the exif orientation and rotates the image accordingly. If an image
	 * can not be read it throws an IOException instead of returning null.
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage read(InputStream input) throws IOException {
		if (input == null) {
			throw new IllegalArgumentException("input == null!");
		}
		//read orientation data
        ImageTransformation dim = null;
        try {
			dim = TrpImgMdParser.readImageDimension(input);
		} catch (ImageProcessingException | MetadataException e) {
			logger.error("Could not extract metadata from stream", e);
		}
		ImageInputStream stream = ImageIO.createImageInputStream(input);
		BufferedImage bi = ImageIO.read(stream);
		if (bi == null) {
			stream.close();
			throw new IOException("Could not read image from input stream.");
		}
		return transformImage(bi, dim);
	}

	/**
	 * This method reads the image data and returns the pixel dimensions not taking into account information on the orientation!
	 * First try {@link TrpImgMdParser#readImageDimension(File)} and use this if no EXIF data was found.
	 * 
	 * @param imgFile
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Dimension readImageDimensions(File imgFile) throws FileNotFoundException, IOException {
		Dimension dim = null;
		ImageInputStream iis = new FileImageInputStream(imgFile);
		final Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
		while (readers.hasNext()) {
			ImageReader reader = readers.next();
			try {
				logger.debug("reader format = " + reader.getFormatName());
				reader.setInput(iis);
				final int xDim = reader.getWidth(0);
				final int yDim = reader.getHeight(0);
				logger.debug("Success with reader impl: " + reader.getClass().getCanonicalName());
				dim = new Dimension(xDim, yDim);
			} catch (Exception e) {
				logger.warn("Could not read image dimensions with reader: " + reader.getFormatName() + ": "
						+ e.getMessage());
				logger.debug("Cause: ", e);
				logger.debug("Reader impl: " + reader.getClass().getCanonicalName());
			} finally {
				reader.dispose();
				iis.close();
			}
			if (dim != null) {
				break;
			}
		}
		if (dim == null) {
			throw new IOException("Could not read image dimensions with ImageIO.");
		}
		return dim;
	}

	public static BufferedImage transformImage(BufferedImage image, ImageTransformation transformation) {
		if (transformation == null || transformation.getExifOrientation() == TrpImgMdParser.DEFAULT_EXIF_ORIENTATION) {
			return image;
		}
		logger.debug("Computing transformation for width = " + image.getWidth() + " height = " + image.getHeight()
				+ " orientation = " + transformation.getExifOrientation());
		AffineTransformOp op = new AffineTransformOp(transformation.getTransformation(), AffineTransformOp.TYPE_BICUBIC);
		BufferedImage destinationImage = new RotatedBufferedImage(transformation, image.getType());
		destinationImage = op.filter(image, destinationImage);
		logger.debug("Destination image: width = " + destinationImage.getWidth() + " height = "
				+ destinationImage.getHeight());

		return destinationImage;
	}

	/**
	 * Java applications register plugins on the classpath automatically.</br>
	 * web apps in Tomcat won't do that.
	 * 
	 * Check issue #4 before using this method!
	 */
	public static void registerImageIOServices() {
		logger.debug("Registering ImageIO readers / writers");
		IIORegistry registry = IIORegistry.getDefaultInstance();

		for (ImageReaderSpi spi : iioReaderList) {
			registerImageIOService(registry, spi, javax.imageio.spi.ImageReaderSpi.class);
		}

		for (ImageWriterSpi spi : iioWriterList) {
			registerImageIOService(registry, spi, javax.imageio.spi.ImageWriterSpi.class);
		}
	}

	public static void unregisterImageIOServices() {
		logger.debug("Unregistering ImageIO readers / writers");
		IIORegistry registry = IIORegistry.getDefaultInstance();

		for (ImageReaderSpi spi : iioReaderList) {
			unregisterImageIOService(registry, spi, javax.imageio.spi.ImageReaderSpi.class);
		}

		for (ImageWriterSpi spi : iioWriterList) {
			unregisterImageIOService(registry, spi, javax.imageio.spi.ImageWriterSpi.class);
		}
	}

	/**
	 * @param registry the ImageIO registry to use
	 * @param provider the service provider object to be registered.
	 * @param category the category under which to register the provider.
	 */
	private static <T extends ImageReaderWriterSpi> void registerImageIOService(IIORegistry registry, T provider,
			Class<T> category) {
		if (registry == null || provider == null || category == null) {
			throw new IllegalArgumentException("An argument is null!");
		}
		if (registry.registerServiceProvider(provider, category)) {
			logger.debug("Registered IIOServiceProvider: " + provider.getPluginClassName());
		} else {
			/*
			 * Actually ImageIO unregistered the old instance of the provider and registered
			 * the new one here!
			 */
			logger.debug("Replaced existing IIOServiceProvider in registry: " + provider.getPluginClassName());
		}
	}

	/**
	 * @param registry the ImageIO registry to use
	 * @param provider the service provider object to be registered.
	 * @param category the category under which to register the provider.
	 */
	private static <T extends ImageReaderWriterSpi> void unregisterImageIOService(IIORegistry registry, T provider,
			Class<T> category) {
		if (registry == null || provider == null || category == null) {
			throw new IllegalArgumentException("An argument is null!");
		}
		if (registry.deregisterServiceProvider(provider, category)) {
			logger.debug("Unregistered IIOServiceProvider: " + provider.getPluginClassName());
		} else {
			/*
			 * Actually ImageIO unregistered the old instance of the provider and registered
			 * the new one here!
			 */
			logger.debug("Unregister failed. IIOServiceProvider was not in registry: " + provider.getPluginClassName());
		}
	}

	/**
	 * this method logs all registered ImageIO service providers and the ClassLoader
	 * they belong to
	 */
	public static void listImageIOServices() {
		IIORegistry registry = IIORegistry.getDefaultInstance();
		logger.info("ImageIO services:");
		Iterator<Class<?>> cats = registry.getCategories();
		while (cats.hasNext()) {
			Class<?> cat = cats.next();
			logger.info("ImageIO category = " + cat);

			Iterator<?> providers = registry.getServiceProviders(cat, true);
			while (providers.hasNext()) {
				Object o = providers.next();
				logger.debug("ImageIO provider of type " + o.getClass().getCanonicalName() + " in "
						+ o.getClass().getClassLoader());
			}
		}
	}

	public static void testReaders() throws IOException {
		String[] formats = { "JPEG", "TIFF", "TIF", "PNG" };
		for (String format : formats) {
			logger.info("Testing readers for format: " + format);
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(format);

			if (!readers.hasNext()) {
				throw new IOException("No reader found for format: " + format);
			}

			while (readers.hasNext()) {
				Object o = readers.next();
				ClassLoader cl = o.getClass().getClassLoader();
				logger.info("ImageReader: " + o + " in " + (cl == null ? "System ClassLoader" : cl));
			}
		}
	}
	
	/**
	 * A subclass of {@link BufferedImage}, that is returned by the read methods in this class in case the EXIF orientation 
	 * tag value required a transformation in the course of loading the image data.<br>
	 * Subsequent processes can get the original width, height and orientation tag value by the {@link #getImageTransformation()()} method.<br>
	 * {@link #getWidth()} and {@link #getHeight()} will return the values for the rotated image and not the ones of the raw image data!
	 *  
	 * @author philip
	 *
	 */
	public static class RotatedBufferedImage extends BufferedImage {
		protected final ImageTransformation transformation;
		public RotatedBufferedImage(ImageTransformation transformation, int imageType) {
			super(transformation.getDestinationWidth(), transformation.getDestinationHeight(), imageType);
			this.transformation = transformation;
		}
		
		public ImageTransformation getImageTransformation() {
			return transformation;
		}
	}
}
