package cc.sukazyo.restools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilesHelper {
	
	/**
	 * 以流的方式复制文件
	 *
	 * @param source 源文件流
	 * @param target 目标文件位置流
	 * @throws IOException 在文件读写时出现错误
	 */
	public static void copyFile (InputStream source, OutputStream target) throws IOException {
		byte[] buf = new byte[1];
		while (source.read(buf) != -1) {
			target.write(buf);
		}
		source.close(); target.close();
	}
	
	/**
	 * 获取目录的绝对路径
	 *
	 * @param dir 目录的File对象
	 *
	 * @return 目录的带最后的斜线的绝对路径
	 */
	public static String getDirectoryAbsolutePath (File dir) {
		Matcher matcher = Pattern.compile("^(.+?)[/\\\\.]?[/\\\\.]?$").matcher(dir.getAbsolutePath());
		if (matcher.find()) {
			return matcher.group(1) + File.separator;
		}
		return dir.getAbsolutePath();
	}
	
}
