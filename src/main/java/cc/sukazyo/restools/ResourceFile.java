package cc.sukazyo.restools;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface ResourceFile extends ResourceEntry {
	
	@Nonnull
	InputStream read () throws IOException;
	
	@Nonnull
	default String readString (Charset charset) throws IOException {
		try (var stream = this.read()) {
			return new String(stream.readAllBytes(), charset);
		}
	}
	
	@Nonnull
	default String readString () throws IOException {
		return readString(StandardCharsets.UTF_8);
	}
	
}
