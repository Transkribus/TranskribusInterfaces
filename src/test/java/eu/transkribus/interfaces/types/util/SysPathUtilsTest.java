package eu.transkribus.interfaces.types.util;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class SysPathUtilsTest {
	
	@Test public void testMultipleAddOfSameDir() throws IOException {
		String dir = "/whatever/dir/";		
		SysPathUtils.addDirToPath(dir);
		String path = SysPathUtils.getPath();
		
		for (int i=0; i<10; ++i) {			
			Assert.assertEquals("Path has changed although adding existing directory!",  path, SysPathUtils.getPath());
		}
	}

	@Test public void testAddDir() throws Exception {
		String dir = "/whatever/dir/";
		String dirC = new File(dir).getCanonicalPath();
		
		SysPathUtils.addDirToPath(dir);
		String path = SysPathUtils.getPath();
		
		Assert.assertTrue("Directory has not been added to path!", path.contains(dirC));
	}

}
