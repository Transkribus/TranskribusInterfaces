package eu.transkribus.interfaces.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlUtilsTest {
	private static final Logger logger = LoggerFactory.getLogger(UrlUtilsTest.class);
	
	final String existingHost = "https://transkribus.eu";
	final String unknownHost = "http://www.asdlakjdalskdjl.com";
	
	@Test 
	public void testUnknownHost() {
		for(int i = 0; i < 10000; i++) {
			try (InputStream is = URLUtils.getInputStream(new URL(existingHost))) {
				//success
			} catch (IOException e) {
				Assert.fail("Could not connect to valid URL: " + existingHost);
			}
			try (InputStream is = URLUtils.getInputStream(new URL(unknownHost))) {
				Assert.fail("Could connect to invalid URL: " + unknownHost);
			} catch (Exception e) {
				logger.debug(e.getClass().getName() + ": " + e.getMessage());
			}
		}
	}
}
