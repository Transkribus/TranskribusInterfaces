package eu.transkribus.interfaces.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for the system PATH
 * 
 * @author sebastianc
 *
 */
public class SysPathUtils {
	private static final Logger logger = LoggerFactory.getLogger(SysPathUtils.class);
	
	public static final String OS_NAME = System.getProperty("os.name").toLowerCase();
	public static final String PATH_VAR_SEPERATOR = isWindows() ? ";" : ":";

	private SysPathUtils() {
	}

	public static boolean isWindows() {
		return (OS_NAME.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS_NAME.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS_NAME.indexOf("nix") >= 0 || OS_NAME.indexOf("nux") >= 0 || OS_NAME.indexOf("aix") > 0);
	}

	public static boolean isSolaris() {
		return (OS_NAME.indexOf("sunos") >= 0);
	}

	public static List<String> splitPath(String path) {
		if(path == null) {
			path = "";
		}
		return (List<String>) Arrays.asList(path.split(PATH_VAR_SEPERATOR));
	}
	
	private static String joinPath(List<String> parts) {
		if(parts == null || parts.isEmpty()) {
			return "";
		}
		return parts.stream()
				.filter(d -> !d.isEmpty())
				.collect(Collectors.joining(PATH_VAR_SEPERATOR));
	}
	
	public static String addDirToPath(String currentPath, String newDir, boolean isSuffix) {
		if(currentPath == null) {
			currentPath = "";
		}
		if(newDir == null) {
			return currentPath;
		}
		
		String newDirCan;
		try {
			newDirCan = new File(newDir).getCanonicalPath();
		} catch (IOException e) {
			logger.error("Cannot add path: " + newDir, e);
			return currentPath;
		}
		//splitPath() returns a fixed-size list!
		List<String> currentParts = new ArrayList<>(splitPath(currentPath));
		if(currentParts.contains(newDirCan)) {
			logger.info("Omitting already included dir '" + newDirCan + "' from being added to path: " + currentPath);
		} else {
			if (isSuffix) {
				currentParts.add(newDirCan);
			} else {
				currentParts.add(0, newDirCan);
			}
		}
		return joinPath(currentParts);
	}

	public static String removeFromPath(String currentPath, String dirToRemove) {
		if(dirToRemove == null || dirToRemove.isEmpty()) {
			return currentPath;
		}		
		return splitPath(currentPath).stream()
			.filter(d -> !dirToRemove.equals(d))
			.collect(Collectors.joining(PATH_VAR_SEPERATOR));
	}
	
	public static String getJavaLibraryPath() {
		String path = System.getProperty("java.library.path");
		return path == null ? "" : path;
	}

	public static void setJavaLibraryPath(String path) {
		System.setProperty("java.library.path", path);

		// set sys_paths to null so that java.library.path will be revalueted next time
		// it is needed
		try {
			final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
			sysPathsField.setAccessible(true);
			sysPathsField.set(null, null);
		} catch (Exception e) {
			System.err.println("Could not set sys_path to null - updated path won't be available in JVM!");
			e.printStackTrace();
		}
	}

	public static boolean removeFromJavaLibraryPath(String dir) {
		if (dir == null) {
			return false;
		}
		String pathOld = getJavaLibraryPath();
		String pathNew = removeFromPath(pathOld, dir);
		setJavaLibraryPath(pathNew);
		return pathOld.length() != pathNew.length();
	}

	public static List<String> getJavaLibraryPathDirs() {
		return splitPath(getJavaLibraryPath());
	}
	
	public static List<String> getJavaLibraryPathDirsCanonical() {
		List<String> dirs = new ArrayList<>();
		for (String d : getJavaLibraryPathDirs()) {
			try {
				dirs.add(new File(d).getCanonicalPath());
			} catch (IOException e) {
				// if filename is invalid, just ignore it... or should I?
			}
		}
		return dirs;
	}

	public static boolean addDirToJavaLibraryPath(String newDir) {
		return addDirToJavaLibraryPath(newDir, true);
	}

	public static boolean addDirToJavaLibraryPath(String newDir, boolean suffixPath) {
		String currentPath = getJavaLibraryPath();
		String newPath = addDirToPath(currentPath, newDir, suffixPath);
		if(!currentPath.equals(newPath)) {
			setJavaLibraryPath(newPath);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Relates to java.library.path System property! Use
	 * {@link #getJavaLibraryPath()} instead.
	 */
	@Deprecated
	public static String getPath() {
		return getJavaLibraryPath();
	}

	/**
	 * Relates to java.library.path System property! Use
	 * {@link #removeFromJavaLibraryPath(String)} instead.
	 */
	@Deprecated
	public static boolean removeFromPath(String dir) {
		return removeFromJavaLibraryPath(dir);
	}

	/**
	 * Relates to java.library.path System property! Use
	 * {@link #getJavaLibraryPathDirs()} instead.
	 */
	@Deprecated
	public static List<String> getPathDirs() {
		return getJavaLibraryPathDirs();
	}

	/**
	 * Relates to java.library.path System property! Use
	 * {@link #getJavaLibraryPathDirsCanonical()} instead.
	 */
	@Deprecated
	public static List<String> getPathDirsCanonical() {
		return getJavaLibraryPathDirsCanonical();
	}

	/**
	 * Relates to java.library.path System property! Use
	 * {@link #addDirToJavaLibraryPath(String)} instead.
	 */
	@Deprecated
	public static boolean addDirToPath(String newDir) {
		return addDirToJavaLibraryPath(newDir);
	}

	/**
	 * Relates to java.library.path System property! Use
	 * {@link #addDirToJavaLibraryPath(String, boolean)} instead.
	 */
	@Deprecated
	public static boolean addDirToPath(String newDir, boolean suffixPath) {
		return addDirToJavaLibraryPath(newDir, suffixPath);
	}

	/**
	 * Relates to java.library.path System property! Use
	 * {@link #setJavaLibraryPath(String)} instead.
	 */
	@Deprecated
	public static void setLibraryPath(String path) {
		setJavaLibraryPath(path);
	}
}
