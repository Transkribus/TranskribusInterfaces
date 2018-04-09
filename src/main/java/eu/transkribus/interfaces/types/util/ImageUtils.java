package eu.transkribus.interfaces.types.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageReaderWriterSpi;
import javax.imageio.stream.ImageInputStream;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
//import org.opencv.highgui.Highgui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi;
import com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi;

import eu.transkribus.interfaces.util.SysPathUtils;

/**
 * A class with static methods with image operations needed for conversion of
 * types
 *
 * @author philip
 *
 */
public class ImageUtils {
	private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

	private static final boolean USE_IMAGE_IO_READ_IMPL = false;
	
    public static BufferedImage convertToBufferedImage(Mat m) {
        // BufferedImage imageBufferedImage = new
        // BufferedImage(imageOpenCVImage.width(), imageOpenCVImage.height(),
        // BufferedImage.TYPE_BYTE_GRAY);
        // // Get BufferedImage's backing array and copy pixels into it
        // byte[] data = ((DataBufferByte)
        // imageBufferedImage.getRaster().getDataBuffer()).getData();
        // imageOpenCVImage.get(0, 0, data);
        // return imageBufferedImage;

        // source:
        // http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
        // Fastest code
        // The output can be assigned either to a BufferedImage or to an Image
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;

    }

    public static BufferedImage convertToBufferedImage(URL u) throws IOException {
        BufferedImage b = read(u);
        logger.debug("read buffered image from url: "+b);
        
        if (b == null) {
            //ImageIO.read() can't handle 302 status code on url
            File tmpFile = ImageUtils.downloadImgFile(u);
            b = read(tmpFile);
            logger.debug("read buffered image from file: "+b);
            if (!tmpFile.delete()) {
                logger.warn("Temp file could not be deleted: " + tmpFile.getAbsolutePath());
            }
        }
        if (b == null) {
            throw new IOException("Could not read buffered image from URL: " + u.toString());
        }
        return b;
    }
    
    public static BufferedImage read(URL u) throws IOException {
    	if(USE_IMAGE_IO_READ_IMPL) {
    		return ImageIO.read(u);
    	} else {
	    	try (
	    			InputStream is = u.openStream();
	    			ImageInputStream iis = ImageIO.createImageInputStream(is)
	    		) {
	    		return read(iis);
	    	}
    	}
    }
    
    public static BufferedImage read(File f) throws IOException {
    	if(USE_IMAGE_IO_READ_IMPL) {
    		return ImageIO.read(f);
    	} else {
	    	try (ImageInputStream iis = ImageIO.createImageInputStream(f)) {
	    		return read(iis);
	    	}
    	}
    }
    
    /**
     * Alternative implementation of ImageIO::read.</br>
     * If one image reader claims to be able to read an ImageInputStream but fails then, 
     * this method, in contrast to the ImageIO impl., will try with other readers before throwing an Exception.
     * 
     * @param iis the ImageInputStream
     * @return a BufferedImage or null only if no reader claimed to support the ImageInputSource
     * @throws IOException
     */
    private static BufferedImage read(ImageInputStream iis) throws IOException {
    	Iterator<ImageReaderSpi> it = IIORegistry.getDefaultInstance().getServiceProviders(ImageReaderSpi.class, true);
    	BufferedImage bi = null;
    	List<String> fails = new LinkedList<>();
    	while(it.hasNext()) {
    		ImageReaderSpi rSpi = it.next();
    		if(rSpi.canDecodeInput(iis)) {
    			logger.debug(rSpi.getPluginClassName() + " claims to support image source.");
    			ImageReader reader = rSpi.createReaderInstance();
    			ImageReadParam param = reader.getDefaultReadParam();
    		    reader.setInput(iis, true, true);
    		    try {
    				bi = reader.read(0, param);
    			} catch(IIOException iioe) {
    				logger.debug(reader.getClass().getCanonicalName() + " failed to read image source!");
    				StackTraceElement ste = iioe.getStackTrace()[0];
    				fails.add(reader.getClass().getCanonicalName() + ": " + iioe.getMessage() 
    					+ " (" + ste.getClassName() + "::" + ste.getMethodName() + " in " + ste.getFileName() + ")");
    			} finally {
    				reader.dispose();
    			}
    		    if(bi != null) {
    		    	break;
    		    }
    		}
    	}
    	if(!fails.isEmpty()) {
    		//Generate an error report
    		StringBuffer sb = new StringBuffer("ImageReaders failed to read the source!");
    		for(String s : fails) {
    			sb.append("\n"+s);
    		}
	    	if(bi == null) {
	    		//All suitable readers failed. Throw IIOExceptions with the report
	    		throw new IIOException(sb.toString());
	    	} else {
	    		//Just log a warning
	    		logger.warn(sb.toString());
	    	}
    	}
    	return bi;
    }

    public static void loadOpenCV() throws Exception {
    	
    	long t0 = System.currentTimeMillis();
    	
    	Properties p = new Properties();
    	
    	p.load(ImageUtils.class.getResourceAsStream("/config.properties"));
    	
    	String opencvlibname="NA";
    	String opencv2lib = "opencv_java2410";
    	
    	String libPath = p.getProperty("OPENCV_LIB_PATH");
    	opencv2lib = p.getProperty("OPENCV2_FALLBACK_LIBNAME");
    	
    	SysPathUtils.addDirToPath(libPath);
//    	System.out.println("PATH = "+SysPathUtils.getPath());
    	
        try {
        	// NOTE: on MACOS the expected file ending for jni libs is .jnilib (instead of .so on linux)
        	// for the System.loadLibrary call
        	// however, if you manually compile and install opencv on mac it seems to produce
        	// an libopencv_java***.so file instead of libopencv_java***.jnilib -> symlink of rename this file to be 
        	// found by the System.loadLibrary call
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//            System.load("/usr/local/share/OpenCV/java/libopencv_java310.so");
            
            opencvlibname = Core.NATIVE_LIBRARY_NAME;
        } catch (java.lang.UnsatisfiedLinkError error) {
        	error.printStackTrace();
        	
            try {
            	logger.error("Could not find "+Core.NATIVE_LIBRARY_NAME+" - trying to load fallback lib "+opencv2lib);
            	
                System.loadLibrary(opencv2lib);
                opencvlibname = opencv2lib;
            } catch (UnsatisfiedLinkError error2) {
//                throw error;
                throw new IOException("Could not find opencv libs "+Core.NATIVE_LIBRARY_NAME+" or "+opencv2lib);
            }
        }
        
        logger.debug("Loaded opencv ("+opencvlibname+") in t = "+(System.currentTimeMillis()-t0));
    }

    public static Mat convertToOpenCvImage(URL u) throws IOException {
        File tmpFile = ImageUtils.downloadImgFile(u);
        
        try {
			loadOpenCV();
		} catch (Exception e) {
			throw new IOException(e);
		}
        
        Mat imageOpenCVImage = null;
        try {
            Class<?> clazz = Class.forName("org.opencv.imgcodecs.Imgcodecs");
            Method method = clazz.getMethod("imread", String.class);
            imageOpenCVImage = (Mat) method.invoke(null, tmpFile.getAbsolutePath());
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            try {
                logger.info("expect OpenCV 2.* - use other static loader");
                Class<?> clazz = Class.forName("org.opencv.highgui.Highgui");
                Method method = clazz.getMethod("imread", String.class);
                imageOpenCVImage = (Mat) method.invoke(null, tmpFile.getAbsolutePath());
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
                throw new RuntimeException("cannot load image '" + tmpFile.getAbsolutePath() + "'", ex);
            }
        }
//        Mat imageOpenCVImage = Highgui.imread(tmpFile.getAbsolutePath());
//        Mat imageOpenCVImage = Imgcodecs.imread(tmpFile.getAbsolutePath());
        if (!tmpFile.delete()) {
            logger.warn("Temp file could not be deleted: " + tmpFile.getAbsolutePath());
        }
        return imageOpenCVImage;
    }

    public static Mat convertToOpenCvImage(BufferedImage b) {
        final byte[] pixels = ((DataBufferByte) b.getRaster().getDataBuffer()).getData();
        Mat m = new Mat(b.getWidth(), b.getHeight(), CvType.CV_8UC3);
        m.put(0, 0, pixels);
        return m;
    }

    public static File downloadImgFile(URL imageUrl) throws IOException {
    	if(!imageUrl.getProtocol().startsWith("http")) {
    		throw new IllegalArgumentException("Only http/https protocol is supported in image URL.");
    	}
        HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(true);
        HttpURLConnection.setFollowRedirects(true);
        conn.connect();

        boolean redirect = false;
        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER) {
                redirect = true;
            }
        }

        if (redirect) {
            // get redirect url from "location" header field
            String newUrl = conn.getHeaderField("Location");
            // open the new connnection again
            conn = (HttpURLConnection) new URL(newUrl).openConnection();
        }

        String filename = null;

        String contentDispHdr = conn.getHeaderField("Content-Disposition");

        if (contentDispHdr != null && contentDispHdr.indexOf("=") != -1) {
            String[] splits = contentDispHdr.split("=");
            if (splits.length > 1) {
                filename = contentDispHdr.split("=")[1].replaceAll("\"", "");
            }
        }
        if (filename == null) {
        	String splits[] = imageUrl.getPath().split("/");
            filename = URLDecoder.decode(splits[splits.length - 1], "UTF-8");
        }
        if (filename == null) {
            filename = "tmp-" + UUID.randomUUID();
        }
        logger.debug("filename = "+filename+" tmpdir = "+System.getProperty("java.io.tmpdir"));
        File output = new File(System.getProperty("java.io.tmpdir") + File.separator + filename);

        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output));
        int inByte;

        while ((inByte = bis.read()) != -1) {
            bos.write(inByte);
        }

        bos.close();
        conn.disconnect();
        return output;
    }

    public static File saveAsFile(Mat m, String path) throws IOException {
        try {
            Class<?> clazz = Class.forName("org.opencv.imgcodecs.Imgcodecs");
            Method method = clazz.getMethod("imwrite", String.class, Mat.class);
            Boolean res = (Boolean) method.invoke(null, path, m);
            if (res != null && res) {
                return new File(path);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            try {
                logger.info("expect OpenCV 2.* - use other static loader");
                Class<?> clazz = Class.forName("org.opencv.highgui.Highgui");
                Method method = clazz.getMethod("imwrite", String.class, Mat.class);
                Boolean res = (Boolean) method.invoke(null, path, m);
                if (res != null && res) {
                    return new File(path);
                }
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
                throw new RuntimeException("Could not write image to: " + path, ex);
            }
        }
        throw new RuntimeException("Could not write image to: " + path + ", return value of writer is null or false");
    }

    public static File saveAsFile(URL u, String path) throws IOException {
        File tmp = downloadImgFile(u);
        File f = new File(path);
        if (tmp.renameTo(f)) {
            return f;
        } else {
            throw new IOException("Could not write image to: " + path);
        }
    }

    public static File saveAsFile(BufferedImage b, String path) throws IOException {
        File f = new File(path);
        //FIXME how to get format name from buffered image
        if (ImageIO.write(b, "jpeg", f)) {
            return f;
        } else {
            throw new IOException("Could not write image to: " + path);
        }
    }
    
    // ImageIO related methods ==========================================
    /**
     * Java applications register plugins on the classpath automatically and in Tomcat the 
     * ContextListener (included in the Twelvemonkeys servlet artifact) can be used for that.</br>
     * Check issue #4 before using this method!
     */
    public static void registerImageIOServices() {
		logger.debug("registering image readers / writers");
		
		IIORegistry registry = IIORegistry.getDefaultInstance();

		// have to programmatically register tiff reader / writer in tomcat
		registerImageIOService(registry, new TIFFImageWriterSpi(), javax.imageio.spi.ImageWriterSpi.class);
		registerImageIOService(registry, new TIFFImageReaderSpi(), javax.imageio.spi.ImageReaderSpi.class);

		registerImageIOService(registry, new com.twelvemonkeys.imageio.plugins.tiff.TIFFImageWriterSpi(),
				javax.imageio.spi.ImageWriterSpi.class);
		registerImageIOService(registry, new com.twelvemonkeys.imageio.plugins.tiff.TIFFImageReaderSpi(),
				javax.imageio.spi.ImageReaderSpi.class);	
	}
	
	/**
	 * @param registry the ImageIO registry to use
	 * @param provider the service provider object to be registered.
	 * @param category the category under which to register the provider.
	 */
	private static <T extends ImageReaderWriterSpi> void registerImageIOService(IIORegistry registry, T provider, Class<T> category) {
		if(registry == null || provider == null || category == null) {
			throw new IllegalArgumentException("An argument is null!");
		}
		if(registry.registerServiceProvider(provider, category)) {
			logger.debug("Registered IIOServiceProvider: " + provider.getPluginClassName());
		} else {
			/*
			 * Actually ImageIO unregistered the old instance of the provider and registered the new one here!
			 */
			logger.debug("Replaced existing IIOServiceProvider in registry: " + provider.getPluginClassName());
		}
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
}
