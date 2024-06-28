package cc.sukazyo.restools.impl.jar.tree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class NodeRoot implements IBranchNode {
	
	@Nonnull
	public final JarFile jar;
	final Map<String, EntryNode> _children = new HashMap<>();
	
	public NodeRoot (@Nonnull JarFile jar) {
		this.jar = jar;
	}
	
	@Nonnull
	@Override
	public JarFile getOwnerJar () {
		return this.jar;
	}
	
	@Nullable
	@Override
	public JarEntry entry () {
		return null;
	}
	
	@Nullable
	@Override
	public IBranchNode parent () {
		return null;
	}
	
	@Nonnull
	@Override
	public Map<String, EntryNode> children () {
		return _children;
	}
	
}
