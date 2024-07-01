package cc.sukazyo.restools.impl.jar;

import cc.sukazyo.restools.ResourceDirectory;
import cc.sukazyo.restools.impl.jar.tree.IBranchNode;
import cc.sukazyo.restools.impl.jar.tree.INode;
import cc.sukazyo.restools.impl.jar.tree.NodeDirectoryEntry;

import javax.annotation.Nonnull;

public class JarDirectory implements IJarDirectory, ResourceDirectory {
	
	public final JarPackage pack;
	public final NodeDirectoryEntry entryNode;
	
	JarDirectory (@Nonnull JarPackage pack, @Nonnull NodeDirectoryEntry entryNode) {
		this.pack = pack;
		this.entryNode = entryNode;
	}
	
	JarDirectory (@Nonnull IJarDirectory parent, String[] path) throws NoSuchEntryException {
		this.pack = parent.getOwnerPackage();
		IBranchNode _parent = parent.getNode();
		for (int i = 0; i < path.length - 1; i++) {
			final INode maybeParent = _parent.getChild(path[i]);
			if (maybeParent == null) throw new NoSuchEntryException();
			if (maybeParent instanceof IBranchNode)
				_parent = (IBranchNode)maybeParent;
			else throw new NoSuchEntryException();
		}
		final INode maybeDirectory = _parent.getChild(path[path.length-1]);
		if (maybeDirectory instanceof NodeDirectoryEntry)
			this.entryNode = (NodeDirectoryEntry)maybeDirectory;
		else throw new NoSuchEntryException();
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
	
}
