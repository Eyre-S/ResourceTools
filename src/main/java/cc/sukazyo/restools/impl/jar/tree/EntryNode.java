package cc.sukazyo.restools.impl.jar.tree;

import javax.annotation.Nonnull;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class EntryNode extends AbsNode implements INode {
	
	@Nonnull
	public final JarEntry entry;
	
	public EntryNode (@Nonnull JarFile jar, @Nonnull JarEntry entry) {
		super(jar);
		this.entry = entry;
	}
	
	@Nonnull
	@Override
	public JarEntry entry () {
		return this.entry;
	}
	
}
