package cc.sukazyo.restools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ResourceEntry {
	
	default boolean isInDirectory () {
		return this.getOwnerPackage().isInDirectory();
	}
	
	default boolean isInJar () {
		return this.getOwnerPackage().isInJar();
	}
	
	@Nonnull
	ResourcePackage getOwnerPackage ();
	
	@Nonnull
	String[] getPath ();
	
	@Nullable
	ResourceDirectory getParentDirectory ();
	
}
