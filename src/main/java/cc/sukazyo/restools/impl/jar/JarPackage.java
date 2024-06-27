package cc.sukazyo.restools.impl.jar;

import cc.sukazyo.restools.ResourceDirectory;
import cc.sukazyo.restools.ResourceFile;
import cc.sukazyo.restools.ResourcePackage;

import javax.annotation.Nonnull;

public class JarPackage implements ResourcePackage {
	
	public JarPackage (Class<?> clazz) {
	}
	
	@Override
	public boolean isInDirectory () {
		return false;
	}
	
	@Override
	public boolean isInJar () {
		return false;
	}
	
	@Nonnull
	@Override
	public ResourceFile getFile (String[] path) {
		return null;
	}
	
	@Nonnull
	@Override
	public ResourceDirectory getDirectory (String[] path) {
		return null;
	}
	
	@Nonnull
	@Override
	public ResourceFile[] listFiles () {
		return new ResourceFile[0];
	}
	
	@Nonnull
	@Override
	public ResourceDirectory[] listDirectories () {
		return new ResourceDirectory[0];
	}
	
}
