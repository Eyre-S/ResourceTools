package cc.sukazyo.restools.impl.jar;

import cc.sukazyo.restools.NoSuchResourceException;
import cc.sukazyo.restools.ResourcePackage;
import cc.sukazyo.restools.impl.jar.tree.IBranchNode;
import cc.sukazyo.restools.impl.jar.tree.NodeRoot;
import cc.sukazyo.restools.impl.jar.tree.Parser;
import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.jar.JarFile;

public class JarPackage implements IJarDirectory, ResourcePackage {
	
	public final JarFile jar;
	public final NodeRoot jarNodeRoot;
	
	public JarPackage (@Nonnull ClassLoader classLoader, @Nonnull String[] identifierFilePath)
	throws UnsupportedPackageTypeException, NoSuchResourceException {
		
		final String identifierStringPath = PathsHelper.compile(identifierFilePath);
		final URL identifierUrl = classLoader.getResource(identifierStringPath);
		if (identifierUrl == null)
			throw new NoSuchResourceException(classLoader, identifierStringPath);
		
		if (!Objects.equals(identifierUrl.getProtocol(), "jar")) throw new UnsupportedPackageTypeException();
		
		try {
			this.jar = new JarFile(new File(
					URI.create(PathsHelper.getJarPath(identifierUrl.toString()))
			));
		} catch (IOException e) {
			throw new IllegalStateException("Failed to read jar file of the request resource " + identifierStringPath + ".", e);
		}
		
		this.jarNodeRoot = Parser.parse(this.jar);
		
	}
	
	@Override
	public boolean isInDirectory () {
		return false;
	}
	
	@Override
	public boolean isInJar () {
		return true;
	}
	
	@Nonnull
	@Override
	public JarPackage getOwnerPackage () {
		return this;
	}
	
	@Nonnull
	@Override
	public IBranchNode getNode () {
		return this.jarNodeRoot;
	}
	
	@Nonnull
	@Override
	public String[] getPath () {
		return ResourcePackage.super.getPath();
	}
	
	@Nullable
	@Override
	public IJarDirectory getParentDirectory () {
		return null;
	}
	
	@Nonnull
	@Override
	public String toString () {
		return "jar:" + jar.getName();
	}
	
}
