package eu.transkribus.interfaces.types.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.transkribus.interfaces.util.HttpUtils;
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
        BufferedImage b = null;
        try {
        	b = TrpImageIO.read(u);
        	logger.debug("read buffered image from url: "+b);
        } catch (IOException e) {
        	logger.error(e.getMessage());
        }
        if (b == null && u.getProtocol().startsWith("http")) {
        	logger.debug("Downloading file from URL...");
        	// TrpImageIO handles redirects and this block may be unnecessary now
            //this was needed as ImageIO.read() can't handle 302 status code on url
            File tmpFile = HttpUtils.downloadFile(u);
            b = TrpImageIO.read(tmpFile);
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

    public static void loadOpenCV() throws Exception {
    	
    	long t0 = System.currentTimeMillis();
    	
    	Properties p = new Properties();
    	
    	p.load(ImageUtils.class.getResourceAsStream("/config.properties"));
    	
    	String opencvlibname="NA";
    	String opencv2lib = "opencv_java2410";
    	
    	String libPath = p.getProperty("OPENCV_LIB_PATH");
    	opencv2lib = p.getProperty("OPENCV2_FALLBACK_LIBNAME");
    	
    	SysPathUtils.addDirToJavaLibraryPath(libPath);
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
    	File tmpFile;
    	if(u.getProtocol().startsWith("file")) {
    		try {
				tmpFile = new File(u.toURI());
			} catch (URISyntaxException e) {
				throw new IOException("Could get file for URL: " + u, e);
			}
    	} else {
    		tmpFile = HttpUtils.downloadFile(u);
    	}
    	
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
        File tmp = HttpUtils.downloadFile(u);
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
     * Java applications register plugins on the classpath automatically.</br>
     * web apps in Tomcat won't do that. 
     * 
     * Check issue #4 before using this method!
     */
    public static void registerImageIOServices() {
		TrpImageIO.registerImageIOServices();
	}
    
    public static void unregisterImageIOServices() {
    	TrpImageIO.unregisterImageIOServices();
    }
	
	/**
	 * this method logs all registered ImageIO service providers and the ClassLoader they belong to
	 */
	public static void listImageIOServices() {
		TrpImageIO.listImageIOServices();
	}

	public static void testReaders() throws IOException {
		TrpImageIO.testReaders();
	}
}
