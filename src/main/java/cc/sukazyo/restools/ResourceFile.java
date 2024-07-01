package cc.sukazyo.restools;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A file in resources. Can be read as an {@link InputStream}, or as a {@link String}.
 * <p>
 * May also be able to use implementation related method to read the metadata of the file.
 *
 * @since 0.3.0
 */
public interface ResourceFile extends ResourceEntry {
	
	/**
	 * Read this file as an {@link InputStream}.
	 *
	 * @return A new {@link InputStream} that reads the content of this file.
	 * @throws IOException If failed to read the content.
	 *
	 * @since 0.3.0
	 */
	@Nonnull
	InputStream read () throws IOException;
	
	/**
	 * Read this file as a plain text file to {@link String}.
	 * <p>
	 * Will read all the content using {@link InputStream#readAllBytes()} and encoded the content
	 * using the specified {@code charset}. Due to all the content is read in memory at one, it
	 * may cause OOM if the content is too large.
	 *
	 * @param charset The charset of the file.
	 * @return The content of this file as a {@link String}.
	 * @throws IOException If any error occurs when {@link #read()}ing this file.
	 *
	 * @since 0.3.0
	 */
	@Nonnull
	default String readString (@Nonnull Charset charset) throws IOException {
		try (InputStream stream = this.read()) {
			return new String(stream.readAllBytes(), charset);
		}
	}
	
	/**
	 * Read this file as a plain text file to {@link String}, using {@link StandardCharsets#UTF_8}
	 * charset.
	 *
	 * @see #readString(Charset)
	 *
	 * @since 0.3.0
	 */
	@Nonnull
	default String readString () throws IOException {
		return readString(StandardCharsets.UTF_8);
	}
	
	/**
	 * Get the parent {@link ResourceDirectory} of this file.
	 * <p>
	 * If the parent of this resource is the root of the classpath, this method will return
	 * the {@link #getOwnerPackage()} of this resource.
	 * <p>
	 * Due to a file cannot be the root of the classpath, it will always have a parent,
	 * so that this method will never return <code>null</code> here.
	 *
	 * @return The parent {@link ResourceDirectory} of this resource.
	 *
	 * @since 0.3.0
	 */
	@Nonnull
	@Override
	ResourceDirectory getParentDirectory ();
	
}
