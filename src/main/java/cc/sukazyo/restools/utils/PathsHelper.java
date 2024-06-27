package cc.sukazyo.restools.utils;

import java.nio.file.Path;
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
	
	public static String[] parseString (String paths) {
		return Arrays.stream(paths.split("[/\\\\]"))
				.filter(x -> !"".equals(x))
				.toArray(String[]::new);
	}
	
	public static String compile (String[] path) {
		return Arrays.stream(path).reduce((a, b) -> a + "/" + b).orElse("");
	}
	
	public static Path getParent (Path current, int level) {
		Path parent = current;
		for (int i = 0; i < level; i++) {
			parent = parent.getParent();
		}
		return parent;
	}
	
	public static String[] dropLast (String[] array, int n) {
		return Arrays.stream(array)
				.limit(array.length - n)
				.toArray(String[]::new);
	}
	
	public static String[] dropLast (String[] array) {
		return dropLast(array, 1);
	}
	
	public static String[] join (String[] original, String... additional) {
		String[] result = new String[original.length + additional.length];
		System.arraycopy(original, 0, result, 0, original.length);
		System.arraycopy(additional, 0, result, original.length, additional.length);
		return result;
	}
	
}
