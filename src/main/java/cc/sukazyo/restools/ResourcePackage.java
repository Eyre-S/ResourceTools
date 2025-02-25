package cc.sukazyo.restools;

import cc.sukazyo.restools.impl.disk.DiskPackage;
import cc.sukazyo.restools.impl.jar.JarPackage;
import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A resource package is a classpath location that contains variables of resources.
 * <p>
 * It basically based on how the resource is compiled and packaged, for example, in Gradle's local run,
 * the project resource is located in the <code>$projectRoot/build/resources/$sourceSet/</code>,
 * or if in a jar, it will just be stored in the root of the jar.
 * <p>
 * So that, a ResourcePackage is also a root {@link ResourceDirectory}.
 *
 * @since 0.3.0
 */
public interface ResourcePackage extends ResourceDirectory, ResourceEntry {
	
	class UnsupportedPackageTypeException extends Exception {}
	
	/**
	 * Get a resource package.
	 * <p>
	 * Different with the v0.2.x and older, the ResourcePackage always locates in the root of the
	 * classpath, instead of the old behavior of the v0.2.x, which locates in the given path.
	 *
	 * @param classLoader the {@link ClassLoader} that is used to load the resource.
	 * @param identifierFilePath Path of a resource, used to locate a resource package.
	 * @return A {@link ResourcePackage} that contains the resource.
	 * @throws UnsupportedPackageTypeException If the resource location cannot be read by any
	 *                                         of the known implementations.
	 * @throws NoSuchResourceException If the identifierFilePath is not exists (either directory
	 *                                 or file) in classloader's all the classpath.
	 *
	 * @since 0.3.0
	 */
	static ResourcePackage get (@Nonnull ClassLoader classLoader, @Nonnull String... identifierFilePath)
	throws UnsupportedPackageTypeException, NoSuchResourceException {
		
		try { return new DiskPackage(classLoader, identifierFilePath); } catch (UnsupportedPackageTypeException ignored) {}
		try { return new JarPackage(classLoader, identifierFilePath); } catch (UnsupportedPackageTypeException ignored) {}
		
		throw new UnsupportedPackageTypeException();
		
	}
	
	/**
	 * Get a resource package.
	 * <p>
	 * Different with the v0.2.x and older, the ResourcePackage always locates in the root of the
	 * classpath, instead of the old behavior of the v0.2.x, which locates in the given path.
	 *
	 * @param classLoader the {@link ClassLoader} that is used to load the resource.
	 * @param identifierFilePath Path of a resource, used to locate a resource package. The path
	 *                           will be parsed to an array using {@link PathsHelper#parseString(String)}.
	 * @return A {@link ResourcePackage} that contains the resource.
	 * @throws UnsupportedPackageTypeException If the resource location cannot be read by any
	 *                                         of the known implementations.
	 * @throws NoSuchResourceException If the identifierFilePath is not exists (either directory
	 *                                 or file) in classloader's all the classpath.
	 *
	 * @since 0.3.0
	 */
	static ResourcePackage get (@Nonnull ClassLoader classLoader, @Nonnull String identifierFilePath)
	throws UnsupportedPackageTypeException, NoSuchResourceException {
		return get(classLoader, PathsHelper.parseString(identifierFilePath));
	}
	
	/**
	 * Get the resource package using the current thread's {@link ClassLoader}.
	 * <p>
	 * This is an alias of {@link #get(ClassLoader, String...)}, in most cases where there's no
	 * custom classloader, this method is enough to work.
	 *
	 * @see #get(ClassLoader, String...)
	 *
	 * @since 0.3.0
	 */
	static ResourcePackage get (@Nonnull String... identifierFilePath)
	throws UnsupportedPackageTypeException, NoSuchResourceException {
		return get(Thread.currentThread().getContextClassLoader(), identifierFilePath);
	}
	
	/**
	 * Get the resource package using the current thread's {@link ClassLoader}.
	 * <p>
	 * This is an alias of {@link #get(ClassLoader, String)}, in most cases where there's no
	 * custom classloader, this method is enough to work.
	 *
	 * @see #get(ClassLoader, String)
	 *
	 * @since 0.3.0
	 */
	static ResourcePackage get (@Nonnull String identifierFilePath)
	throws UnsupportedPackageTypeException, NoSuchResourceException {
		return get(PathsHelper.parseString(identifierFilePath));
	}
	
	/**
	 * Get the associated {@link ResourcePackage} that contains this {@link ResourceDirectory}.
	 * <p>
	 * In a resource package, it will always get itself.
	 *
	 * @return itself.
	 *
	 * @since 0.3.0
	 */
	@Nonnull
	default ResourcePackage getOwnerPackage () {
		return this;
	}
	
	/**
	 * Get the parent {@link ResourceDirectory directory} of the current directory.
	 * <p>
	 * In a {@link ResourcePackage}, due to it is always the top level of the classpath, so that
	 * it is always the top level directory of the resources, it will always be null.
	 *
	 * @return always null.
	 *
	 * @since 0.3.0
	 */
	@Nullable
	@Override
	default ResourceDirectory getParentDirectory () {
		return null;
	}
	
	/**
	 * Get the path of the current directory, relatively with the root of the classpath.
	 * <p>
	 * In a {@link ResourcePackage}, due to it is always the top level of the classpath, so that
	 * it is always the top level directory of the resources, it will always be an empty array.
	 *
	 * @return An array that contains the path of the current directory. In a {@link ResourcePackage},
	 *         this always be an empty array.
	 *
	 * @since 0.3.0
	 */
	@Nonnull
	@Override
	default String[] getPath () {
		return new String[]{};
	}
	
	/**
	 * Is the resources in this package stored in the local filesystem.
	 * <p>
	 * A resource package in the local filesystem should be implemented by {@link DiskPackage}.
	 * In most cases, it may be under some development environment, and in most cases the resources
	 * are separated from the classes.
	 * <p>
	 * Relatively, under current version, if this is not the case, it should be {@link #isInJar()}.
	 *
	 * @return <code>true</code> if it is in the local filesystem, <code>false</code> otherwise.
	 *
	 * @since 0.3.0
	 */
	boolean isInDirectory ();
	
	/**
	 * Is the resource in this package stored in a jar.
	 * <p>
	 * A resource package in the jar should be implemented by {@link JarPackage}. The resources
	 * (in most cases, along with the classes in the same project) seem to be already packaged
	 * in the jar, and the root directory of the classpath should be the root directory of the
	 * jar.
	 * <p>
	 * Relatively, under current version, if this is not the case, it should be {@link #isInDirectory()}.
	 *
	 * @return <code>true</code> if it is in the jar, <code>false</code> otherwise.
	 *
	 * @since 0.3.0
	 */
	boolean isInJar ();
	
	/**
	 * Where this resource package is stored.
	 * <p>
	 * It returns a String, separated with a `:`, the first part indicates this package's
	 * implementation type, the second part is the path that where can find this resource package.
	 *
	 * @since 0.3.0
	 */
	@Nonnull
	String toString ();
	
}
