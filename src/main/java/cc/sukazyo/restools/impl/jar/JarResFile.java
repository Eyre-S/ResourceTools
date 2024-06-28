package cc.sukazyo.restools.impl.jar;

import cc.sukazyo.restools.ResourceDirectory;
import cc.sukazyo.restools.ResourceFile;
import cc.sukazyo.restools.impl.jar.tree.IBranchNode;
import cc.sukazyo.restools.impl.jar.tree.NodeDirectoryEntry;
import cc.sukazyo.restools.impl.jar.tree.NodeFileEntry;
import cc.sukazyo.restools.impl.jar.tree.NodeRoot;
import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

public class JarResFile implements ResourceFile {
	
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
			var maybeParent = _parent.getChild(path[i]);
			if (maybeParent == null) throw new NullPointerException();
			if (maybeParent instanceof IBranchNode nowParent)
				_parent = nowParent;
			else throw new NullPointerException();
		}
		var maybeDirectory = _parent.getChild(path[path.length-1]);
		if (maybeDirectory instanceof NodeFileEntry nowFile)
			this.entryNode = nowFile;
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
	public String[] getPath () {
		return PathsHelper.parseString(this.entryNode.entry().getName());
	}
	
	@Nullable
	@Override
	public ResourceDirectory getParentDirectory () {
		final var parentNode = this.entryNode.parent();
		if (parentNode instanceof NodeRoot)
			return this.pack;
		return new JarDirectory(this.pack, (NodeDirectoryEntry)this.entryNode.parent());
	}
	
}
