package cc.sukazyo.restools.impl.jar.tree;

import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public interface INode {
	
	@Nonnull
	JarFile getOwnerJar();
	@Nullable
	JarEntry entry();
	
	@Nullable
	IBranchNode parent();
	
	@Nonnull
	String getAbsolutePath ();
	
	@Nonnull
	default String[] getPath () {
		final IBranchNode parent = this.parent();
		if (parent == null)
			return new String[]{};
		else return PathsHelper.join(parent.getPath(), this.getName());
	}
	
	@Nonnull
	default String getName () {
		final IBranchNode parent = this.parent();
		if (parent == null)
			return "";
		else {
			final JarEntry entry = this.entry();
			if (entry == null)
				throw new IllegalStateException();
			final String absPath = this.getAbsolutePath();
			return absPath.substring(
					parent.getAbsolutePath().length(),
					entry.isDirectory() ? absPath.length() - 1 : absPath.length()
			);
		}
	}
	
}
