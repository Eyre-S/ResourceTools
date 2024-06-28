package cc.sukazyo.restools.impl.jar.tree;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;

public class NodeDirectoryEntry extends EntryNode implements IBranchNode, INode {
	
	public final IBranchNode _parent;
	
	final Map<String, EntryNode> _children = new HashMap<>();
	
	public NodeDirectoryEntry (@Nonnull IBranchNode _parent, @Nonnull JarEntry _entry) {
		super(_parent.getOwnerJar(), _entry);
		this._parent = _parent;
	}
	
	@Nonnull
	@Override
	public Map<String, EntryNode> children () {
		return _children;
	}
	
	@Nonnull
	@Override
	public IBranchNode parent () {
		return this._parent;
	}
	
}
