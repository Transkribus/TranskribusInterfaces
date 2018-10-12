package eu.transkribus.interfaces.types.util;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import eu.transkribus.interfaces.util.SysPathUtils;

public class SysPathUtilsTest {

	@Test
	public void testMultipleAddOfSameDir() throws IOException {
		String dir = "/whatever/dir/";
		SysPathUtils.addDirToJavaLibraryPath(dir);
		String path = SysPathUtils.getJavaLibraryPath();

		for (int i = 0; i < 10; ++i) {
			Assert.assertEquals("Path has changed although adding existing directory!", path,
					SysPathUtils.getJavaLibraryPath());
		}
	}

	@Test
	public void testAddDir() throws Exception {
		String dir = "/whatever/dir/";
		String dirC = new File(dir).getCanonicalPath();

		SysPathUtils.addDirToJavaLibraryPath(dir);
		String path = SysPathUtils.getJavaLibraryPath();

		Assert.assertTrue("Directory has not been added to path!", path.contains(dirC));
	}

	@Test
	public void removeDir() {
		String initPath = "/usr/local/lib:/my/path/to/remove:/this/path/is/just/nonsense";
		String alteredPath = SysPathUtils.removeFromPath(initPath, "/my/path/to/remove");
		Assert.assertEquals("/usr/local/lib:/this/path/is/just/nonsense", alteredPath);		
	}
}
