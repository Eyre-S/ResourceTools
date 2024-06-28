package cc.sukazyo.restools.utils;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.Arrays;

public class PathsHelper {
	
	@Nonnull
	public static String getJarPath (@Nonnull String path) {
		final int jarIdentifier = path.lastIndexOf("/") - 1;
		if (jarIdentifier > 0 && path.startsWith("jar:"))
			return path.substring("jar:".length(), jarIdentifier);
		else return path;
	}
	
	@Nonnull
	public static String[] parseString (@Nonnull String paths) {
		return Arrays.stream(paths.split("[/\\\\]"))
				.filter(x -> !"".equals(x))
				.toArray(String[]::new);
	}
	
	@Nonnull
	public static String compile (@Nonnull String[] path) {
		return Arrays.stream(path).reduce((a, b) -> a + "/" + b).orElse("");
	}
	
	@Nonnull
	public static Path getParent (@Nonnull Path current, @Nonnegative int level) {
		Path parent = current;
		for (int i = 0; i < level; i++) {
			parent = parent.getParent();
		}
		return parent;
	}
	
	@Nonnull
	public static String[] dropLast (@Nonnull String[] array, @Nonnegative int n) {
		return Arrays.stream(array)
				.limit(Math.max(array.length - n, 0))
				.toArray(String[]::new);
	}
	
	@Nonnull
	public static String[] dropLast (@Nonnull String[] array) {
		return dropLast(array, 1);
	}
	
	@Nonnull
	public static String[] join (@Nonnull String[] original, @Nonnull String... additional) {
		String[] result = new String[original.length + additional.length];
		System.arraycopy(original, 0, result, 0, original.length);
		System.arraycopy(additional, 0, result, original.length, additional.length);
		return result;
	}
	
}
