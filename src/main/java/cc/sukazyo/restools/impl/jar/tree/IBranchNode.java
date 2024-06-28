package cc.sukazyo.restools.impl.jar.tree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public interface IBranchNode extends INode {
	
	@Nonnull
	Map<String, EntryNode> children ();
	
	@Nullable
	default INode getChild (@Nonnull String name) {
		return this.children().get(name);
	}
	
	@Nonnull
	default NodeFileEntry[] getChildrenFiles () {
		return this.children().values().stream()
					.filter(NodeFileEntry.class::isInstance)
					.map(NodeFileEntry.class::cast)
					.toArray(NodeFileEntry[]::new);
	}
	
	@Nonnull
	default NodeDirectoryEntry[] getChildrenDirectories () {
		return this.children().values().stream()
					.filter(NodeDirectoryEntry.class::isInstance)
					.map(NodeDirectoryEntry.class::cast)
					.toArray(NodeDirectoryEntry[]::new);
	}
	
}
