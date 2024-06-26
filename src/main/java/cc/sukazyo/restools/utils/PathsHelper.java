package cc.sukazyo.restools.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathsHelper {
	
	
	/**
	 * Normalize paths to the path that with NO starting slash and WITH trailing slash.
	 * <p>
	 * like this: <code>path/to/code/</code>
	 * <p>
	 * For directory paths only!
	 */
	public static String cutPath (String path) {
		Matcher matcher = Pattern.compile("^[/\\\\]?(.+?)[/\\\\]?$").matcher(path);
		if (matcher.find()) {
			path = matcher.group(1) + "/";
		}
		return path;
	}
	
	public static String[] pathStringToArray (String paths) {
		return Arrays.stream(paths.split("[/\\\\]"))
				.filter(x -> !"".equals(x))
				.toArray(String[]::new);
	}
	
}
