package eu.transkribus.interfaces.types.util;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import eu.transkribus.interfaces.util.SysPathUtils;

public class SysPathUtilsTest {

	final static String SEP = SysPathUtils.PATH_VAR_SEPERATOR;
	
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
		String initPath = "/usr/local/lib" + SEP + "/my/path/to/remove" + SEP + "/this/path/is/just/nonsense";
		String alteredPath = SysPathUtils.removeFromPath(initPath, "/my/path/to/remove");
		Assert.assertEquals("/usr/local/lib" + SEP + "/this/path/is/just/nonsense", alteredPath);		
	}
	
	@Test
	public void testRelativePathPrefixing() {
		String initPath = "/usr/local/lib" + SEP + "some/dir/in/home" + SEP + "/this/path/is/just/nonsense";
		String expected = "/usr/local/lib" + SEP + "/home/user/some/dir/in/home" + SEP + "/this/path/is/just/nonsense";
		String prefix = "/home/user/";
		Assert.assertEquals(expected, SysPathUtils.addPrefixToRelativePaths(initPath, prefix));
		prefix = "/home/user";
		Assert.assertEquals(expected, SysPathUtils.addPrefixToRelativePaths(initPath, prefix));
	}
	
	@Test
	public void testAddDirs() {
		String initPath = "/usr/local/lib";
		String[] toAdd = {"/first/dir/to/add", "/second/dir/to/add" + SEP + "/and/another", "/third/dir"};
		final String expectedAfterPrefix = "/first/dir/to/add" + SEP + "/second/dir/to/add" + SEP + "/and/another" + SEP + "/third/dir" + SEP + "/usr/local/lib";
		final String expectedAfterSuffix = "/usr/local/lib" + SEP + "/first/dir/to/add" + SEP + "/second/dir/to/add" + SEP + "/and/another" + SEP + "/third/dir";
		Assert.assertEquals(expectedAfterPrefix, SysPathUtils.addDirToPath(initPath, false, toAdd));
		Assert.assertEquals(expectedAfterSuffix, SysPathUtils.addDirToPath(initPath, true, toAdd));
	}
}
