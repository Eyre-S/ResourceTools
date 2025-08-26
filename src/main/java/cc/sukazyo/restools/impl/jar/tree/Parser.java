package cc.sukazyo.restools.impl.jar.tree;

import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Parser {
	
	@Nonnull
	public static NodeRoot parse (@Nonnull JarFile jar) {
		
		final NodeRoot root = new NodeRoot(jar);
		
		List<JarEntry> unresolvedEntries;
		
		{
			final List<JarEntry> firstUnresolvedEntries = new ArrayList<>();
			jar.stream().forEach(entry -> resolveEntry(root, entry, firstUnresolvedEntries));
			unresolvedEntries = firstUnresolvedEntries;
		}
		
		while (!unresolvedEntries.isEmpty()) {
			
			final List<JarEntry> newUnresolvedEntries = new ArrayList<>();
			unresolvedEntries.forEach(entry -> resolveEntry(root, entry, newUnresolvedEntries));
			if (unresolvedEntries.size() == newUnresolvedEntries.size()) {
//				throw new IllegalStateException("Failed to resolve all entries due to thw following entries have no parents: " + unresolvedEntries);
				for (JarEntry entry : newUnresolvedEntries) {
					root._dangling_entries.put(entry.getName(), entry);
				}
				break;
			}
			unresolvedEntries = newUnresolvedEntries;
			
		}
		
		return root;
		
	}
	
	private static void resolveEntry (NodeRoot root, JarEntry entry, List<JarEntry> unresolvedStack) {
		
		final String[] entryPath = PathsHelper.parseString(entry.getName());
		
		IBranchNode parent = root;
		for (int i = 0; i < entryPath.length - 1; i++) {
			final INode maybeParent = parent.getChild(entryPath[i]);
			if (maybeParent == null) {
				unresolvedStack.add(entry);
				return;
			}
			else if (maybeParent instanceof IBranchNode)
				parent = (IBranchNode)maybeParent;
			else {
				throw new IllegalStateException(String.format(
						"Failed to create entry node '%s' due to the parent '%s' is not a directory node.",
						entry.getName(), entryPath[i]
				));
			}
		}
		if (entry.isDirectory()) {
			parent.children().put(entryPath[entryPath.length-1], new NodeDirectoryEntry(parent, entry));
		} else {
			parent.children().put(entryPath[entryPath.length-1], new NodeFileEntry(parent, entry));
		}
		
	}
	
}
