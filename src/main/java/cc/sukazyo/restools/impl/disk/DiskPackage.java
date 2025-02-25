package cc.sukazyo.restools.impl.disk;

import cc.sukazyo.restools.NoSuchResourceException;
import cc.sukazyo.restools.ResourcePackage;
import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiskPackage implements ResourcePackage, IDiskDirectory, IDiskEntry {
	
	@Nonnull
	public final Path packageRoot;
	
	public DiskPackage (@Nonnull ClassLoader classLoader, @Nonnull String[] identifierFilePath)
	throws UnsupportedPackageTypeException, NoSuchResourceException {
		
		final String resPath = PathsHelper.compile(identifierFilePath);
		final int resLevel = identifierFilePath.length;
		final URL resURL = classLoader.getResource(resPath);
		if (resURL == null)
			throw new NoSuchResourceException(classLoader, resPath);
		
		if (!"file".equals(resURL.getProtocol()))
			throw new UnsupportedPackageTypeException();
		
		final Path identifierRealPath;
		try {
			identifierRealPath = Paths.get(resURL.toURI());
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Cannot get the root dir of the required resource " + resPath + ".", e);
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
		return null;
	}
	
	@Nonnull
	@Override
	public String toString () {
		return "dir:" + this.packageRoot;
	}
	
}
