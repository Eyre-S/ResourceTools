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
	
	private DiskFile(@Nonnull DiskPackage pack, @Nonnull String[] path, @Nonnull Path file)
	throws DiskFileUnavailableException {
		this.pack = pack;
		this.path = path;
		this.file = file;
		if (!file.toFile().isFile())
			throw new DiskFileUnavailableException();
	}
	
	DiskFile (@Nonnull DiskPackage pack, @Nonnull Path parent_dir, @Nonnull String[] path)
	throws DiskFileUnavailableException {
		this(
				pack, path,
				Paths.get(parent_dir.toString(), path)
		);
	}
	
	/**
	 * Create a new file based on the parent {@link IDiskDirectory} and the target {@link File}.
	 *
	 * @param parent the parent {@link IDiskDirectory} of this file.
	 * @param target the target {@link File} of this file, should be the one-level file in the
	 *               parent directory.
	 * @throws DiskFileUnavailableException if the target file is unavailable.
	 */
	DiskFile (@Nonnull IDiskDirectory parent, @Nonnull File target)
	throws DiskFileUnavailableException {
		this(
				parent.getOwnerPackage(),
				PathsHelper.join(parent.getPath(), target.getName()),
				target.toPath()
		);
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
		try {
			return new DiskDirectory(this.pack, this.file, this.path, 1);
		} catch (DiskDirectory.GoUpMeetsTopException e) {
			return e.pack;
		}
	}
	
}
