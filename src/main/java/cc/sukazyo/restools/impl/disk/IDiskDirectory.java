package cc.sukazyo.restools.impl.disk;

import cc.sukazyo.restools.ResourceDirectory;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;

public interface IDiskDirectory extends ResourceDirectory, IDiskEntry {
	
	@Nonnull
	@Override
	default DiskFile getFile (String... path) {
		return new DiskFile(this.getOwnerPackage(), this.getRealPath(), path);
	}
	
	@Nonnull
	@Override
	default IDiskDirectory getDirectory (String... path) {
		return new DiskDirectory(this.getOwnerPackage(), this.getRealPath(), path);
	}
	
	@Nonnull
	@Override
	default DiskFile[] listFiles () {
		File[] filesAll = this.getRealPath().toFile().listFiles(File::isFile);
		assert filesAll != null;
		return Arrays.stream(filesAll)
					 .map(file -> new DiskFile(this.getOwnerPackage(), this, file))
					 .toArray(DiskFile[]::new);
	}
	
	@Nonnull
	@Override
	default IDiskDirectory[] listDirectories () {
		File[] filesAll = this.getRealPath().toFile().listFiles(File::isDirectory);
		assert filesAll != null;
		return Arrays.stream(filesAll)
				.map(file -> new DiskDirectory(this.getOwnerPackage(), this, file))
				.toArray(DiskDirectory[]::new);
	}
	
}
