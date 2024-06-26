package cc.sukazyo.restools.impl.pack;

import cc.sukazyo.restools.ResourceDirectory;
import cc.sukazyo.restools.ResourceFile;
import cc.sukazyo.restools.ResourcePackage;

import javax.annotation.Nonnull;
import java.io.File;

public class DirectoryPackage implements ResourcePackage {
	
	public final File packageRoot;
	
	public DirectoryPackage (Class<?> clazz) {
		
		//noinspection DataFlowIssue
		if (!"file".equals(clazz.getResource("").getProtocol()))
			throw new RuntimeException("!! Only file protocol is supported"); // TODO
		
		//noinspection DataFlowIssue
		packageRoot = new File(clazz.getResource("").getPath());
		
	}
	
	@Override
	public boolean isInDirectory () {
		return true;
	}
	
	@Override
	public boolean isInJar () {
		return false;
	}
	
	@Nonnull
	@Override
	public ResourceFile getFile (String... path) {
		return null;
	}
	
	@Nonnull
	@Override
	public ResourceDirectory getDirectory (String... path) {
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
