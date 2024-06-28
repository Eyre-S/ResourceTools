package cc.sukazyo.restools.impl.jar.tree;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public interface INode {
	
	@Nonnull
	JarFile getOwnerJar();
	@Nullable
	JarEntry entry();
	
	@Nullable
	IBranchNode parent();
	
}
