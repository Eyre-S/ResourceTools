package cc.sukazyo.restools.impl.jar;

import cc.sukazyo.restools.ResourceDirectory;
import cc.sukazyo.restools.ResourceFile;
import cc.sukazyo.restools.impl.jar.tree.IBranchNode;
import cc.sukazyo.restools.impl.jar.tree.NodeDirectoryEntry;
import cc.sukazyo.restools.impl.jar.tree.NodeRoot;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public interface IJarDirectory extends ResourceDirectory, IJarEntry {
	
	@Nonnull
	IBranchNode getNode ();
	
	@Nullable
	@Override
	default JarResFile getFile (@Nonnull String... path) {
		try {
			return new JarResFile(this, path);
		} catch (NoSuchEntryException e) {
			return null;
		}
	}
	
	@Nullable
	@Override
	default JarDirectory getDirectory (@Nonnull String... path) {
		try {
			return new JarDirectory(this, path);
		} catch (NoSuchEntryException e) {
			return null;
		}
	}
	
	@Nonnull
	@Override
	default ResourceFile[] listFiles () {
		return Arrays.stream(this.getNode().getChildrenFiles())
					 .map(x -> new JarResFile(this.getOwnerPackage(), x))
					 .toArray(JarResFile[]::new);
	}
	
	@Nonnull
	@Override
	default JarDirectory[] listDirectories () {
		return Arrays.stream(this.getNode().getChildrenDirectories())
					 .map(x -> new JarDirectory(this.getOwnerPackage(), x))
					 .toArray(JarDirectory[]::new);
	}
	
	@Nullable
	@Override
	default IJarDirectory getParentDirectory () {
		IBranchNode parent = this.getNode().parent();
		if (parent == null)
			return null;
		if (parent instanceof NodeRoot)
			return this.getOwnerPackage();
		if (parent instanceof NodeDirectoryEntry)
			return new JarDirectory(this.getOwnerPackage(), (NodeDirectoryEntry)parent);
		throw new IllegalStateException();
	}
	
	
}
