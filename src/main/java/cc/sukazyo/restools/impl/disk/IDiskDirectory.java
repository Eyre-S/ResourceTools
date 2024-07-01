package cc.sukazyo.restools.impl.disk;

import cc.sukazyo.restools.ResourceDirectory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Arrays;

public interface IDiskDirectory extends ResourceDirectory, IDiskEntry {
	
	@Nullable
	@Override
	default DiskFile getFile (@Nonnull String... path) {
		try {
			return new DiskFile(this.getOwnerPackage(), this.getRealPath(), path);
		} catch (DiskFileUnavailableException e) {
			return null;
		}
	}
	
	@Nullable
	@Override
	default IDiskDirectory getDirectory (@Nonnull String... path) {
		try {
			return new DiskDirectory(this.getOwnerPackage(), this.getRealPath(), path);
		} catch (DiskFileUnavailableException e) {
			return null;
		}
	}
	
	@Nonnull
	@Override
	default DiskFile[] listFiles () {
		final File[] filesAll = this.getRealPath().toFile().listFiles(File::isFile);
		assert filesAll != null;
		return Arrays.stream(filesAll)
					 .map(file -> {
						 try {
							 return new DiskFile(this, file);
						 } catch (DiskFileUnavailableException e) {
							 throw new IllegalStateException(e);
						 }
					 })
					 .toArray(DiskFile[]::new);
	}
	
	@Nonnull
	@Override
	default IDiskDirectory[] listDirectories () {
		final File[] filesAll = this.getRealPath().toFile().listFiles(File::isDirectory);
		assert filesAll != null;
		return Arrays.stream(filesAll)
				.map(file -> {
					try {
						return new DiskDirectory(this, file);
					} catch (DiskFileUnavailableException e) {
						throw new IllegalStateException(e);
					}
				})
				.toArray(DiskDirectory[]::new);
	}
	
}
