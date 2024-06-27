package cc.sukazyo.restools.impl.disk;

import cc.sukazyo.restools.ResourceDirectory;
import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiskDirectory implements IDiskDirectory, IDiskEntry, ResourceDirectory {
	
	public final DiskPackage pack;
	
	public final String[] path;
	public final Path directory;
	
	public DiskDirectory (DiskPackage pack, Path parent_dir, String[] path) {
		this.pack = pack;
		this.path = path;
		this.directory = Paths.get(parent_dir.toString(), path);
	}
	
	DiskDirectory (DiskPackage pack, IDiskDirectory parent, File target) {
		this.pack = pack;
		this.path = PathsHelper.join(parent.getPath(), target.getName());
		this.directory = target.toPath();
	}
	
	public static class UpTopException extends Exception {
		public final DiskPackage pack;
		public UpTopException (DiskPackage pack) {
			super("Got to the resource package top level directory when trying to go up.");
			this.pack = pack;
		}
	}
	
	protected DiskDirectory (DiskPackage pack, Path parent_dir, String[] old_path, int upLevels) throws UpTopException {
		final String[] newPath = PathsHelper.dropLast(old_path, upLevels);
		if (newPath.length == 0) throw new UpTopException(pack);
		this.pack = pack;
		this.path = newPath;
		this.directory = PathsHelper.getParent(parent_dir, upLevels);
	}
	
	@Nonnull
	@Override
	public Path getRealPath () {
		return this.directory;
	}
	
	@Nonnull
	@Override
	public DiskPackage getOwnerPackage () {
		return this.pack;
	}
	
	@Nonnull
	@Override
	public String[] getPath () {
		return this.path;
	}
	
	@Nullable
	@Override
	public IDiskDirectory getParentDirectory () {
		try {
			return new DiskDirectory(this.pack, this.directory, this.path, 1);
		} catch (UpTopException e) {
			return e.pack;
		}
	}
	
}
