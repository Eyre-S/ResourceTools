package cc.sukazyo.restools.impl.jar;

import cc.sukazyo.restools.ResourceEntry;
import cc.sukazyo.restools.impl.jar.tree.INode;

import javax.annotation.Nonnull;

public interface IJarEntry extends ResourceEntry {
	
	@Nonnull
	@Override
	JarPackage getOwnerPackage ();
	
	@Nonnull
	INode getNode ();
	
	@Nonnull
	@Override
	default String[] getPath () {
		return this.getNode().getPath();
	}
	
}
