package cc.sukazyo.restools.impl.jar.tree;

import javax.annotation.Nonnull;
import java.util.jar.JarFile;

public abstract class AbsNode implements INode {
	
	@Nonnull
	public final JarFile jar;
	
	public AbsNode (@Nonnull JarFile jar) {
		this.jar = jar;
	}
	
	@Nonnull
	@Override
	public JarFile getOwnerJar () {
		return this.jar;
	}
	
}
