package cc.sukazyo.restools.impl.jar.tree;

import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;
import java.util.jar.JarFile;

public class Parser {
	
	@Nonnull
	public static NodeRoot parse (@Nonnull JarFile jar) {
		
		NodeRoot root = new NodeRoot(jar);
		
		jar.stream().forEach(entry -> {
			
			final String[] entryPath = PathsHelper.parseString(entry.getName());
			
			IBranchNode parent = root;
			for (int i = 0; i < entryPath.length - 1; i++) {
				var maybeParent = parent.getChild(entryPath[i]);
				if (maybeParent == null)
					throw new IllegalStateException("Failed to create entry node '%s' due to the parent '%s' cannot be found.".formatted(
							entry.getName(), entryPath[i]
					));
				if (maybeParent instanceof IBranchNode parentBranch) {
					parent = parentBranch;
					continue;
				}
				throw new IllegalStateException("Failed to create entry node '%s' due to the parent '%s' is not a directory node.".formatted(
						entry.getName(), entryPath[i]
				));
			}
			if (entry.isDirectory()) {
				parent.children().put(entryPath[entryPath.length-1], new NodeDirectoryEntry(parent, entry));
			} else {
				parent.children().put(entryPath[entryPath.length-1], new NodeFileEntry(parent, entry));
			}
			
		});
		
		return root;
		
	}
	
}
