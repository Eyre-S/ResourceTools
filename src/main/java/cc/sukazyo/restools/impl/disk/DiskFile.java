package cc.sukazyo.restools.impl.disk;

import cc.sukazyo.restools.ResourceFile;
import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiskFile implements ResourceFile, IDiskEntry {
	
	@Nonnull
	public final DiskPackage pack;
	@Nonnull
	public final String[] path;
	@Nonnull
	public final Path file;
	
	public DiskFile (@Nonnull DiskPackage pack, @Nonnull Path parent_dir, @Nonnull String[] path) {
		this.pack = pack;
		this.path = path;
		this.file = Paths.get(parent_dir.toString(), path);
	}
	
	DiskFile (@Nonnull DiskPackage pack, @Nonnull IDiskDirectory parent, @Nonnull File target) {
		this.pack = pack;
		this.path = PathsHelper.join(parent.getPath(), target.getName());
		this.file = target.toPath();
	}
	
	@Nonnull
	@Override
	public InputStream read () throws IOException {
		return Files.newInputStream(file);
	}
	
	@Nonnull
	@Override
	public DiskPackage getOwnerPackage () {
		return this.pack;
	}
	
	@Nonnull
	@Override
	public Path getRealPath () {
		return this.file;
	}
	
	@Nonnull
	@Override
	public String[] getPath () {
		return this.path;
	}
	
	@Nonnull
	@Override
	public IDiskDirectory getParentDirectory () {
		return pack.getDirectory(PathsHelper.dropLast(this.path));
	}
	
}
