package cc.sukazyo.restools.impl.jar.tree;

import javax.annotation.Nonnull;
import java.util.jar.JarEntry;

public class NodeFileEntry extends EntryNode implements INode {
	
	@Nonnull
	public final IBranchNode _parent;
	
	public NodeFileEntry (@Nonnull IBranchNode _parent, @Nonnull JarEntry entry) {
		super(_parent.getOwnerJar(), entry);
		this._parent = _parent;
	}
	
	@Nonnull
	@Override
	public IBranchNode parent () {
		return this._parent;
	}
	
}
