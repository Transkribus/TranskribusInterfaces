package eu.transkribus.interfaces.types.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
//import org.opencv.highgui.Highgui;

/**
 * A class with static methods with image operations needed for conversion of
 * types
 *
 * @author philip
 *
 */
public class ImageUtils {

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
        BufferedImage b = ImageIO.read(u);
        if (b == null) {
            //ImageIO.read() can't handle 302 status code on url
            File tmpFile = ImageUtils.downloadImgFile(u);
            b = ImageIO.read(tmpFile);
            if (!tmpFile.delete()) {
                System.out.println("Temp file could not be deleted: " + tmpFile.getAbsolutePath());
            }
        }
        if (b == null) {
            throw new IOException("Could read buffered image from URL: " + u.toString());
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
            	System.out.println("Could not find "+Core.NATIVE_LIBRARY_NAME+" - trying to load fallback lib "+opencv2lib);
            	
                System.loadLibrary(opencv2lib);
                opencvlibname = opencv2lib;
            } catch (UnsatisfiedLinkError error2) {
//                throw error;
                throw new IOException("Could not find opencv libs "+Core.NATIVE_LIBRARY_NAME+" or "+opencv2lib);
            }
        }
        
        System.out.println("Successfully loaded opencv: "+opencvlibname);
        System.out.println("t = "+(System.currentTimeMillis()-t0));
    }
    
//    static {
//    	try {
//			loadOpenCV();
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
//    }

    public static Mat convertToOpenCvImage(URL u) throws IOException {
        File tmpFile = ImageUtils.downloadImgFile(u);
        
        try {
			loadOpenCV();
		} catch (Exception e) {
			throw new IOException(e);
		}
        
//        try {
//            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        } catch (java.lang.UnsatisfiedLinkError error) {
//            try {
//                System.loadLibrary("opencv_java2410");
//            } catch (UnsatisfiedLinkError error2) {
////                throw error;
//                throw new IOException("Could not find opencv: "+Core.NATIVE_LIBRARY_NAME+"/"+"opencv_java2410");
//            }
//        }
        
        Mat imageOpenCVImage = null;
        try {
            Class clazz = Class.forName("org.opencv.imgcodecs.Imgcodecs");
            Method method = clazz.getMethod("imread", String.class);
            imageOpenCVImage = (Mat) method.invoke(null, tmpFile.getAbsolutePath());
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            try {
                System.out.println("expect OpenCV 2.* - use other static loader");
                Class clazz = Class.forName("org.opencv.highgui.Highgui");
                Method method = clazz.getMethod("imread", String.class);
                imageOpenCVImage = (Mat) method.invoke(null, tmpFile.getAbsolutePath());
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
                throw new RuntimeException("cannot load image '" + tmpFile.getAbsolutePath() + "'", ex);
            }
        }
//        Mat imageOpenCVImage = Highgui.imread(tmpFile.getAbsolutePath());
//        Mat imageOpenCVImage = Imgcodecs.imread(tmpFile.getAbsolutePath());
        if (!tmpFile.delete()) {
            System.out.println("Temp file could not be deleted: " + tmpFile.getAbsolutePath());
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
        System.out.println(filename);
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
            Class clazz = Class.forName("org.opencv.imgcodecs.Imgcodecs");
            Method method = clazz.getMethod("imwrite", String.class, Mat.class);
            Boolean res = (Boolean) method.invoke(null, path, m);
            if (res != null && res) {
                return new File(path);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            try {
                System.out.println("expect OpenCV 2.* - use other static loader");
                Class clazz = Class.forName("org.opencv.highgui.Highgui");
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
}
