package cc.sukazyo.restools.impl.disk;

import cc.sukazyo.restools.ResourceEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;

public interface IDiskEntry extends ResourceEntry {
	
	
	@Nonnull
	Path getRealPath ();
	
	@Nonnull
	@Override
	DiskPackage getOwnerPackage ();
	
	@Nullable
	@Override
	IDiskDirectory getParentDirectory ();
	
}
