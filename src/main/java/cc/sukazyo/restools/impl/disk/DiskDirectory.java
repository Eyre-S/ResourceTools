package cc.sukazyo.restools.impl.disk;

import cc.sukazyo.restools.ResourceDirectory;
import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiskDirectory implements IDiskDirectory, IDiskEntry, ResourceDirectory {
	
	@Nonnull
	public final DiskPackage pack;
	
	@Nonnull
	public final String[] path;
	@Nonnull
	public final Path directory;
	
	private DiskDirectory (@Nonnull DiskPackage pack, @Nonnull String[] path, @Nonnull Path directory)
	throws DiskFileUnavailableException {
		this.pack = pack;
		this.path = path;
		this.directory = directory;
		if (!this.directory.toFile().isDirectory())
			throw new DiskFileUnavailableException();
	}
	
	DiskDirectory (@Nonnull DiskPackage pack, @Nonnull Path parent_dir, @Nonnull String[] path)
	throws DiskFileUnavailableException {
		this(
				pack, path,
				Paths.get(parent_dir.toString(), path)
		);
	}
	
	/**
	 * Create a new directory based on the parent {@link IDiskDirectory} and the target {@link File}.
	 *
	 * @param parent the parent {@link IDiskDirectory} of this file.
	 * @param target the target {@link File} of this directory, should be the one-level child
	 *               directory in the parent directory.
	 * @throws DiskFileUnavailableException if the target file is unavailable.
	 */
	DiskDirectory (@Nonnull IDiskDirectory parent, @Nonnull File target)
	throws DiskFileUnavailableException {
		this(
				parent.getOwnerPackage(),
				PathsHelper.join(parent.getPath(), target.getName()),
				target.toPath()
		);
	}
	
	public static class GoUpMeetsTopException extends Exception {
		@Nonnull public final DiskPackage pack;
		public GoUpMeetsTopException (@Nonnull DiskPackage pack) {
			super("Got to the resource package top level directory when trying to go up.");
			this.pack = pack;
		}
	}
	
	protected DiskDirectory (
			@Nonnull DiskPackage pack, @Nonnull Path parent_dir,
			@Nonnull String[] old_path, @Nonnegative int upLevels
	) throws GoUpMeetsTopException {
		final String[] newPath = PathsHelper.dropLast(old_path, upLevels);
		if (newPath.length == 0) throw new GoUpMeetsTopException(pack);
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
	
	@Nonnull
	@Override
	public IDiskDirectory getParentDirectory () {
		try {
			return new DiskDirectory(this.pack, this.directory, this.path, 1);
		} catch (GoUpMeetsTopException e) {
			return e.pack;
		}
	}
	
}
