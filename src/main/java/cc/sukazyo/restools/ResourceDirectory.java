package cc.sukazyo.restools;

import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;

public interface ResourceDirectory extends ResourceEntry {
	
	@Nonnull
	ResourceFile getFile (String... path);
	@Nonnull
	default ResourceFile getFile (String path) {
		return this.getFile(PathsHelper.pathStringToArray(path));
	}
	
	@Nonnull
	ResourceDirectory getDirectory(String... path);
	@Nonnull
	default ResourceDirectory getDirectory(String path) {
		return this.getDirectory(PathsHelper.pathStringToArray(path));
	}
	
	@Nonnull
	ResourceFile[] listFiles();
	
	@Nonnull
	ResourceDirectory[] listDirectories();
	
}
