package cc.sukazyo.restools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An entry in the resources, maybe a directory, or a file.
 *
 * @since 0.3.0
 */
public interface ResourceEntry {
	
	/**
	 * Is the resource stored in the local file system.
	 * <p>
	 * Normally it should be the same with its {@link #getOwnerPackage()}'s
	 * {@link ResourcePackage#isInDirectory()}
	 *
	 * @see ResourcePackage#isInDirectory()
	 *
	 * @since 0.3.0
	 */
	default boolean isInDirectory () {
		return this.getOwnerPackage().isInDirectory();
	}
	
	/**
	 * Is the resource stored in a jar.
	 * <p>
	 * Normally it should be the same with its {@link #getOwnerPackage()}'s
	 * {@link ResourcePackage#isInJar()}.
	 *
	 * @see ResourcePackage#isInJar()
	 *
	 * @since 0.3.0
	 */
	default boolean isInJar () {
		return this.getOwnerPackage().isInJar();
	}
	
	/**
	 * Get which {@link ResourcePackage} contains this resource. Or says this resource belongs
	 * to which {@link ResourcePackage}.
	 *
	 * @return A {@link ResourcePackage} that contains this resource.
	 */
	@Nonnull
	ResourcePackage getOwnerPackage ();
	
	/**
	 * Get the String array formatted path of this resource.
	 * <p>
	 * A String array formatted path is a String array for representing a relative path, every
	 * element in the array is a dir level in the path. For example, array <code>{"a", "b"}</code>
	 * indicates the traditional path <code>a/b</code>.
	 * <p>
	 * This method returns a path that is relative to the root of the classpath (aka. the root
	 * of the resource package, or just {@link ResourcePackage}). So that a {@link ResourcePackage}
	 * will always returns an empty array.
	 *
	 * @return A path of this resource that is relative to the root of the classpath.
	 *
	 * @since 0.3.0
	 */
	@Nonnull
	String[] getPath ();
	
	/**
	 * Get the parent {@link ResourceDirectory} of this resource.
	 * <p>
	 * If the parent of this resource is the root of the classpath, this method will return
	 * the {@link #getOwnerPackage()} of this resource.
	 * <p>
	 * If this is already the root of the classpath, this method will return <code>null</code>.
	 *
	 * @return The parent {@link ResourceDirectory} of this resource. Or null if this is already
	 *         the root directory.
	 *
	 * @since 0.3.0
	 */
	@Nullable
	ResourceDirectory getParentDirectory ();
	
}
