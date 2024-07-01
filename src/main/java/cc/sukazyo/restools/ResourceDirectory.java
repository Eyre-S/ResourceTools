package cc.sukazyo.restools;

import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A directory in the resources.
 * <p>
 * A Resource Directory is like a normal directory, contains some child files (which will be
 * {@link ResourceFile}, contains data and contains no children), or some child directories
 * (which will be another {@link ResourceDirectory}, can contain other child files and child
 * directories). It may also be a root directory, and the root directory in a resource package
 * is the {@link ResourcePackage} itself.
 * <p>
 * The child files and child directories can be called children of this directory. All the
 * children's parents must be this directory. A directory may also contain no children.
 */
public interface ResourceDirectory extends ResourceEntry {
	
	/**
	 * Get a {@link ResourceFile} based on this directory.
	 *
	 * @param path A String array formatted path to the file, relatively with this directory.
	 * @return A {@link ResourceFile} that the path indicates, or null if not found.
	 *
	 * @since 0.3.0
	 */
	@Nullable
	ResourceFile getFile (@Nonnull String... path);
	/**
	 * Get a {@link ResourceFile} based on this directory.
	 *
	 * @param path Path to a file, relatively with this directory. This path should be a
	 *             traditional relative path but should not use <code>./</code> at the beginning,
	 *             will be formatted to the String array formatted path using {@link PathsHelper#parseString(String)}.
	 * @return A {@link ResourceFile} that the path indicates, or null if not found.
	 *
	 * @since 0.3.0
	 */
	@Nullable
	default ResourceFile getFile (@Nonnull String path) {
		return this.getFile(PathsHelper.parseString(path));
	}
	
	/**
	 * Get a {@link ResourceDirectory} based on this directory.
	 *
	 * @param path A String array formatted path to the directory, relatively with this directory.
	 * @return A {@link ResourceDirectory} that the path indicates, or null if not found.
	 *
	 * @since 0.3.0
	 */
	@Nullable
	ResourceDirectory getDirectory(@Nonnull String... path);
	/**
	 * Get a {@link ResourceFile} based on this directory.
	 *
	 * @param path Path to a file, relatively with this directory. This path should be a
	 *             traditional relative path but should not use <code>./</code> at the beginning,
	 *             will be formatted to the String array formatted path using {@link PathsHelper#parseString(String)}.
	 * @return A {@link ResourceFile} that the path indicates, or null if not found.
	 *
	 * @since 0.3.0
	 */
	@Nullable
	default ResourceDirectory getDirectory(@Nonnull String path) {
		return this.getDirectory(PathsHelper.parseString(path));
	}
	
	/**
	 * Get all children files in this directory.
	 * <p>
	 * Only one level's children files can be listed, recursively contained files will not be
	 * listed.
	 *
	 * @return A list that contains all children files in this directory. Maybe empty if this
	 *         directory contains no children files directly.
	 *
	 * @since 0.3.0
	 */
	@Nonnull
	ResourceFile[] listFiles();
	
	/**
	 * Get all children directories in this directory.
	 * <p>
	 * Only one level's children directories can be listed, recursively contained directories
	 * will not be listed.
	 *
	 * @return A list that contains all children directories in this directory. Maybe empty if
	 *         this directory contains no child directories directly.
	 *
	 * @since 0.3.0
	 */
	@Nonnull
	ResourceDirectory[] listDirectories();
	
}
