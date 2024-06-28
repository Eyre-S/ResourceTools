package cc.sukazyo.restools.impl.jar;

import cc.sukazyo.restools.ResourceDirectory;
import cc.sukazyo.restools.impl.jar.tree.IBranchNode;
import cc.sukazyo.restools.impl.jar.tree.NodeDirectoryEntry;
import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;

public class JarDirectory implements IJarDirectory, ResourceDirectory {
	
	public final JarPackage pack;
	public final NodeDirectoryEntry entryNode;
	
	JarDirectory (@Nonnull JarPackage pack, @Nonnull NodeDirectoryEntry entryNode) {
		this.pack = pack;
		this.entryNode = entryNode;
	}
	
	JarDirectory (@Nonnull IJarDirectory parent, String[] path) {
		this.pack = parent.getOwnerPackage();
		IBranchNode _parent = parent.getNode();
		for (int i = 0; i < path.length - 1; i++) {
			var maybeParent = _parent.getChild(path[i]);
			if (maybeParent == null) throw new NullPointerException();
			if (maybeParent instanceof IBranchNode nowParent)
				_parent = nowParent;
			else throw new NullPointerException();
		}
		var maybeDirectory = _parent.getChild(path[path.length-1]);
		if (maybeDirectory instanceof NodeDirectoryEntry nowDirectory)
			this.entryNode = nowDirectory;
		else throw new NullPointerException();
	}
	
	@Nonnull
	@Override
	public JarPackage getOwnerPackage () {
		return this.pack;
	}
	
	@Nonnull
	@Override
	public IBranchNode getNode () {
		return this.entryNode;
	}
	
	@Nonnull
	@Override
	public String[] getPath () {
		return PathsHelper.parseString(this.entryNode.entry().getName());
	}
	
}
