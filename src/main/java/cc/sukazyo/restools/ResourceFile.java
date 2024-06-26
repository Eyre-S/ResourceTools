package cc.sukazyo.restools;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface ResourceFile extends ResourceEntry {
	
	@Nonnull
	InputStream read ();
	
	@Nonnull
	String readString (Charset charset);
	
	@Nonnull
	default String readString () {
		return readString(StandardCharsets.UTF_8);
	}
	
}
