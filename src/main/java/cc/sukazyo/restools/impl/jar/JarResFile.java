package cc.sukazyo.restools.impl.jar;

import cc.sukazyo.restools.ResourceDirectory;
import cc.sukazyo.restools.ResourceFile;
import cc.sukazyo.restools.impl.jar.tree.*;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

public class JarResFile implements ResourceFile, IJarEntry {
	
	public final JarPackage pack;
	public final NodeFileEntry entryNode;
	
	JarResFile (JarPackage pack, NodeFileEntry entryNode) {
		this.pack = pack;
		this.entryNode = entryNode;
	}
	
	JarResFile (@Nonnull IJarDirectory parent, String[] path) {
		this.pack = parent.getOwnerPackage();
		IBranchNode _parent = parent.getNode();
		for (int i = 0; i < path.length - 1; i++) {
			INode maybeParent = _parent.getChild(path[i]);
			if (maybeParent == null) throw new NullPointerException();
			if (maybeParent instanceof IBranchNode)
				_parent = (IBranchNode)maybeParent;
			else throw new NullPointerException();
		}
		final INode maybeFile = _parent.getChild(path[path.length-1]);
		if (maybeFile instanceof NodeFileEntry)
			this.entryNode = (NodeFileEntry)maybeFile;
		else throw new NullPointerException();
	}
	
	@Nonnull
	@Override
	public InputStream read () throws IOException {
		return this.pack.jar.getInputStream(this.entryNode.entry());
	}
	
	@Nonnull
	@Override
	public JarPackage getOwnerPackage () {
		return this.pack;
	}
	
	@Nonnull
	@Override
	public INode getNode () {
		return this.entryNode;
	}
	
	@Nonnull
	@Override
	public ResourceDirectory getParentDirectory () {
		final IBranchNode parentNode = this.entryNode.parent();
		if (parentNode instanceof NodeRoot)
			return this.pack;
		return new JarDirectory(this.pack, (NodeDirectoryEntry)this.entryNode.parent());
	}
	
}
