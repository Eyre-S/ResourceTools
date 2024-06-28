package cc.sukazyo.restools.impl.jar;

import cc.sukazyo.restools.ResourceDirectory;
import cc.sukazyo.restools.ResourceFile;
import cc.sukazyo.restools.impl.jar.tree.IBranchNode;
import cc.sukazyo.restools.impl.jar.tree.NodeDirectoryEntry;
import cc.sukazyo.restools.impl.jar.tree.NodeRoot;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public interface IJarDirectory extends ResourceDirectory {
	
	@Nonnull
	@Override
	JarPackage getOwnerPackage ();
	
	@Nonnull
	IBranchNode getNode ();
	
	@Nonnull
	@Override
	default JarResFile getFile (@Nonnull String... path) {
		return new JarResFile(this, path);
	}
	
	@Nonnull
	@Override
	default JarDirectory getDirectory (@Nonnull String... path) {
		return new JarDirectory(this, path);
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
		var parent = this.getNode().parent();
		if (parent == null)
			return null;
		if (parent instanceof NodeRoot)
			return this.getOwnerPackage();
		if (parent instanceof NodeDirectoryEntry parentDirNode)
			return new JarDirectory(this.getOwnerPackage(), parentDirNode);
		throw new IllegalStateException();
	}
	
	
}
