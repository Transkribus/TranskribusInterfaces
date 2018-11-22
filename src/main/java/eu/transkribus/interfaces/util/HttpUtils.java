package eu.transkribus.interfaces.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods for streaming data from URLs.<br>
 * {@link URL#openStream()}, which is used by ImageIO internally, does not follow redirects and thus would fail on 30x status codes.
 *
 *	TODO: Test multiple redirects
 */
public class HttpUtils {
	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	private HttpUtils() {}

	/**
	 * Download the file to java.io.tmpdir
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static File downloadFile(URL url) throws IOException {
    	if(!url.getProtocol().startsWith("http")) {
    		throw new IllegalArgumentException("Only http/https protocol is supported in image URL.");
    	}
        final HttpURLConnection conn = openConnection(url);

        String filename = determineFilename(conn);
        logger.debug("filename = "+filename+" tmpdir = "+System.getProperty("java.io.tmpdir"));
        File output = new File(System.getProperty("java.io.tmpdir") + File.separator + filename);

        try (BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
        		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(output));) {
	        int inByte;
	
	        while ((inByte = bis.read()) != -1) {
	            bos.write(inByte);
	        }
        }
        conn.disconnect();
        return output;
    }

	/**
	 * Get an inputstream for that URL, following redirects
	 * @param imageUrl
	 * @return
	 * @throws IOException
	 */
	public static InputStream getInputStream(URL imageUrl) throws IOException {
    	if(!imageUrl.getProtocol().startsWith("http")) {
    		return imageUrl.openStream();
    	}
    	final HttpURLConnection conn = openConnection(imageUrl);
        FilterInputStream fis = new FilterInputStream(conn.getInputStream()) {
        	@Override
        	public void close() throws IOException {
        		super.close();
        		conn.disconnect();
        	}
        };
        return fis;
    }
	
	private static HttpURLConnection openConnection(URL url) throws IOException {
    	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(true);
        HttpURLConnection.setFollowRedirects(true);
        conn.connect();
        
        if (isRedirect(conn)) {
            // get redirect url from "location" header field
            String newUrl = conn.getHeaderField("Location");
            logger.debug("Setting redirect to: " + newUrl);
            // open the new connnection again
            conn = (HttpURLConnection) new URL(newUrl).openConnection();
        }
        
		return conn;
	}
	
	private static boolean isRedirect(HttpURLConnection conn) throws IOException {
		boolean redirect = false;
        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER) {
                redirect = true;
            }
        }
        return redirect;
	}
	
	/** 
	 * Extract filename from Content-Disposition Header if applicable. 
	 * Otherwise try to parse a filename from the URL string ending.
	 * If both methods fail, a name in the form "tmp-{randomUUID}" is generated.
	 * @param conn
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String determineFilename(HttpURLConnection conn) throws UnsupportedEncodingException {
		String filename = null;
        String contentDispHdr = conn.getHeaderField("Content-Disposition");

        if (contentDispHdr != null && contentDispHdr.indexOf("=") != -1) {
            String[] splits = contentDispHdr.split("=");
            if (splits.length > 1) {
                filename = contentDispHdr.split("=")[1].replaceAll("\"", "");
            }
        }
        if (filename == null) {
        	String splits[] = conn.getURL().getPath().split("/");
            filename = URLDecoder.decode(splits[splits.length - 1], "UTF-8");
        }
        if (filename == null) {
            filename = "tmp-" + UUID.randomUUID();
        }
		return filename;
	}
}
