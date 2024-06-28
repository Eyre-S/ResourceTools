package cc.sukazyo.restools.impl.disk;

import cc.sukazyo.restools.ResourceFile;
import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiskFile implements ResourceFile, IDiskEntry {
	
	public final DiskPackage pack;
	public final String[] path;
	public final Path file;
	
	public DiskFile (DiskPackage pack, Path parent_dir, String[] path) {
		this.pack = pack;
		this.path = path;
		this.file = Paths.get(parent_dir.toString(), path);
	}
	
	DiskFile (DiskPackage pack, IDiskDirectory parent, File target) {
		this.pack = pack;
		this.path = PathsHelper.join(parent.getPath(), target.getName());
		this.file = target.toPath();
	}
	
	@Nonnull
	@Override
	public InputStream read () throws IOException {
		return new FileInputStream(file.toFile());
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
