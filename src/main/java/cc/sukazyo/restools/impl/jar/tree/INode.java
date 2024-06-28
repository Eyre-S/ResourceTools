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
		var parent = this.parent();
		if (parent == null)
			return new String[]{};
		else return PathsHelper.join(parent.getPath(), this.getName());
	}
	
	@Nonnull
	default String getName () {
		var parent = this.parent();
		if (parent == null)
			return "";
		else
			return this.getAbsolutePath().substring(
					parent.getAbsolutePath().length(),
					this.entry().isDirectory() ? this.getAbsolutePath().length() - 1 : this.getAbsolutePath().length()
			);
	}
	
}
