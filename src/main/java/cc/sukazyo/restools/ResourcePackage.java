package cc.sukazyo.restools;

import cc.sukazyo.restools.impl.disk.DiskPackage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ResourcePackage extends ResourceDirectory, ResourceEntry {
	
	class UnsupportedPackageTypeException extends Exception {}
	
	static ResourcePackage get (ClassLoader classLoader, String... identifierFilePath) throws UnsupportedPackageTypeException {
		
		try { return new DiskPackage(classLoader, identifierFilePath); } catch (UnsupportedPackageTypeException ignored) {}
//		try { return new JarPackage(identifierFilePath); } catch (UnsupportedPackageTypeException ignored) {}
		
		throw new UnsupportedPackageTypeException();
		
	}
	
	static ResourcePackage get (String... identifierFilePath) throws UnsupportedPackageTypeException {
		return get(Thread.currentThread().getContextClassLoader(), identifierFilePath);
	}
	
	@Nonnull
	default ResourcePackage getOwnerPackage () {
		return this;
	}
	
	@Nullable
	@Override
	default ResourceDirectory getParentDirectory () {
		return null;
	}
	
	@Nonnull
	@Override
	default String[] getPath () {
		return new String[]{};
	}
	
	boolean isInDirectory ();
	
	boolean isInJar ();
	
	@Nonnull
	String toString ();
	
}
