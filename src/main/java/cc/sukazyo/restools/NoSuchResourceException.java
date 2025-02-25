package cc.sukazyo.restools;

import javax.annotation.Nonnull;

public class NoSuchResourceException extends RuntimeException {
	
	public NoSuchResourceException (@Nonnull ClassLoader classLoader, @Nonnull String identifierFilePath) {
		super(String.format(
				"Cannot find resource \"%s\"! Such path does not exists in the class loader %s.",
				identifierFilePath, classLoader
		));
	}
	
}
