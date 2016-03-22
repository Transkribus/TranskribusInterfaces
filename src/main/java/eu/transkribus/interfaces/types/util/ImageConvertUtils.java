package eu.transkribus.interfaces.types.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.opencv.core.Mat;

public class ImageConvertUtils {

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
					|| status == HttpURLConnection.HTTP_SEE_OTHER)
				redirect = true;
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
			filename = splits[splits.length - 1];
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

}
