package cc.sukazyo.restools.utils;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Arrays;

public class PathsHelper {
	
	/**
	 * From a URL that indicates a file in one jar, get the URL path of that jar.
	 * <p>
	 * Like, from <code>jar:file:/home/me/test.jar!/file</code>, get the jar path
	 * <code>file:/home/me/test.jar</code>.
	 * <p>
	 * Normally expected a URL that starts with the <code>jar:</code> protocol head, and a
	 * <code>!</code> indicates the following path is in the jar. This method will remove the
	 * starting <code>jar:</code> and all the paths after the last <code>!</code> indicate to
	 * get the jar path.
	 * <p>
	 * If this jar is in another jar, then the path may be like
	 * <code>jar:jar:file:/home/me/test.jar!/libs/inner.jar!/my_assets</code>. In this case,
	 * this method will only delete the FIRST ONE <code>jar:</code> and the LAST ONE <code>!</code>
	 * indicate so that it will get the real path of the INNER jar but not the outer jar. The
	 * final path will be like <code>jar:file:/home/me/test.jar!/libs/inner.jar</code>.
	 *
	 * @param path a jar protocol URL in String format. indicates a file in the required jar.
	 * @return the URL of the required jar, in String format.
	 *         <p>
	 *         If there seems not a jar protocol URL, then the original URL will be returned.
	 */
	@Nonnull
	public static String getJarPath (@Nonnull String path) {
		final int jarIdentifier = path.lastIndexOf("!/");
		if (jarIdentifier > 0 && path.startsWith("jar:"))
			return path.substring("jar:".length(), jarIdentifier);
		else return path;
	}
	
	/**
	 * Parse a String format path to a String array formated path.
	 * <p>
	 * This method simply split the String by <code>/</code> or <code>\</code>, and remove all
	 * the empty elements to proper process inputs like <code>/a//b/c</code>.
	 *
	 * @param paths String formatted path, traditionally separate dir levels with <code>/</code>
	 *              or <code>\</code>.
	 * @return a String array formated path. Every array element is a dir level in the path.
	 */
	@Nonnull
	public static String[] parseString (@Nonnull String paths) {
		return Arrays.stream(paths.split("[/\\\\]"))
				.filter(x -> !"".equals(x))
				.toArray(String[]::new);
	}
	
	/**
	 * Compile the String array formatted path to the traditional String format path.
	 * <p>
	 * This method simply join all the elements with <code>/</code>.
	 */
	@Nonnull
	public static String compile (@Nonnull String[] path) {
		return Arrays.stream(path).reduce((a, b) -> a + "/" + b).orElse("");
	}
	
	/**
	 * Get the n levels parent of a path by calling {@link Path#getParent()} n times.
	 *
	 * @return the n levels parent of the current path. Due to the {@link Path#getParent()}
	 *         method can be null when it reaches the top, this method has a chance to be null to.
	 */
	@Nullable
	public static Path getParent (@Nonnull Path current, @Nonnegative int level) {
		Path parent = current;
		for (int i = 0; i < level; i++) {
			parent = parent.getParent();
		}
		return parent;
	}
	
	/**
	 * Get the n levels parent of a String array formatted path, by drop the last n elements
	 * of the path array.
	 * <p>
	 * If the path array is shorter than n, then it will return an empty array.
	 *
	 * @param array the path array which we want to get the n levels parent.
	 * @param n how many to drop, or how many levels to go up in the path.
	 * @return A new array which is the n levels parent of the current path.
	 *         <p>
	 *         If the path array is shorter than n, then it will return an empty array.
	 */
	@Nonnull
	public static String[] dropLast (@Nonnull String[] array, @Nonnegative int n) {
		return Arrays.stream(array)
				.limit(Math.max(array.length - n, 0))
				.toArray(String[]::new);
	}
	
	/**
	 * Drop the last element of the String array, or says get the parent path of the path.
	 *
	 * @param array a path formatted in String-array.
	 * @return A new array, contains the old array without the last element, or says, the parent path of the current path.
	 * @see #dropLast(String[], int) the implementation
	 */
	@Nonnull
	public static String[] dropLast (@Nonnull String[] array) {
		return dropLast(array, 1);
	}
	
	/**
	 * Append additional elements to the original String array.
	 *
	 * @param original The original array.
	 * @param additional The elements to append.
	 * @return A new array that is the original array with the additional elements.
	 */
	@Nonnull
	public static String[] join (@Nonnull String[] original, @Nonnull String... additional) {
		String[] result = new String[original.length + additional.length];
		System.arraycopy(original, 0, result, 0, original.length);
		System.arraycopy(additional, 0, result, original.length, additional.length);
		return result;
	}
	
}
