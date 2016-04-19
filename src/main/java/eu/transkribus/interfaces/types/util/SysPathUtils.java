package eu.transkribus.interfaces.types.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for the system PATH
 * @author sebastianc
 *
 */
public class SysPathUtils {
	public static String OS_NAME = System.getProperty("os.name").toLowerCase();
	public static String PATH_VAR_SEPERATOR = isWindows() ? ";" : ":";
	
	public static boolean isWindows() {
		return (OS_NAME.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS_NAME.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS_NAME.indexOf("nix") >= 0 || OS_NAME.indexOf("nux") >= 0 || OS_NAME.indexOf("aix") > 0 );
	}

	public static boolean isSolaris() {
		return (OS_NAME.indexOf("sunos") >= 0);
	}
	
    public static String getPath() {
    	String path = System.getProperty("java.library.path");
    	return path==null ? "" : path;
    }
    
    public static List<String> getPathDirs() {    	
    	return (List<String>) Arrays.asList(getPath().split(PATH_VAR_SEPERATOR));
    }
    
    public static List<String> getPathDirsCanonical() {
    	List<String> dirs = new ArrayList<>();
    	for (String d : getPathDirs()) {
    		try {
				dirs.add(new File(d).getCanonicalPath());
			} catch (IOException e) {
				// if filename is invalid, just ignore it... or should I?
			}
    	}
    	return dirs;
    }
    
    public static boolean addDirToPath(String newDir) {
    	String newDirCan;
		try {
			newDirCan = new File(newDir).getCanonicalPath();
		} catch (IOException e) {
			System.err.println("Cannot invalide path: "+newDir+" - "+e.getMessage());
			return false;
		}

    	// check if directory is already in path
    	if (getPathDirsCanonical().contains(newDirCan))
    		return false;
    	
    	setLibraryPath(getPath()+PATH_VAR_SEPERATOR+newDirCan);
    	return true;
    }
    
	public static void setLibraryPath(String path) {
		System.setProperty("java.library.path", path);

		// set sys_paths to null so that java.library.path will be revalueted next time it is needed
		try {
			final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
			sysPathsField.setAccessible(true);
			sysPathsField.set(null, null);
		} catch (Exception e) {
			System.err.println("Could not set sys_path to null - updated path won't be available in JVM!");
			e.printStackTrace();
		}
	}
	
}
