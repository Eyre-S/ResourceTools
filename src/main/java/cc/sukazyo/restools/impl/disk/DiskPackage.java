package cc.sukazyo.restools.impl.disk;

import cc.sukazyo.restools.ResourcePackage;
import cc.sukazyo.restools.utils.FilesHelper;
import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiskPackage implements ResourcePackage, IDiskDirectory, IDiskEntry {
	
	public final Path packageRoot;
	
	public DiskPackage (ClassLoader classLoader, @Nonnull String[] identifierFilePath)
	throws UnsupportedPackageTypeException {
		
		final String resPath = PathsHelper.compile(identifierFilePath);
		final int resLevel = identifierFilePath.length;
		final var resURL = classLoader.getResource(resPath);
		assert resURL != null : "Cannot find resource: " + resPath;
		
		if (!"file".equals(resURL.getProtocol()))
			throw new UnsupportedPackageTypeException();
		
		Path identifierRealPath;
		try {
			identifierRealPath = Paths.get(resURL.toURI());
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Cannot get resource's package due to an internal error.", e);
		}
		this.packageRoot = PathsHelper.getParent(identifierRealPath, resLevel);
		
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
	public Path getRealPath () {
		return this.packageRoot;
	}
	
	@Nonnull
	@Override
	public DiskPackage getOwnerPackage () {
		return this;
	}
	
	@Nullable
	@Override
	public IDiskDirectory getParentDirectory () {
		return this;
	}
	
	@Nonnull
	@Override
	public String toString () {
		return "dir:" + FilesHelper.encode(this.packageRoot.toString());
	}
	
}
