package cc.sukazyo.restools;

import cc.sukazyo.restools.impl.pack.DirectoryPackage;
import cc.sukazyo.restools.impl.pack.JarPackage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ResourcePackage extends ResourceDirectory, ResourceEntry {
	
	static ResourcePackage get (Class<?> clazz) {
		
		try { return new DirectoryPackage(clazz); } catch (Exception ignored) {}
		try { return new JarPackage(clazz); } catch (Exception ignored) {}
		
		throw new RuntimeException("Unsupported resource type"); // TODO
		
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
