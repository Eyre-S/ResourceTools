package cc.sukazyo.restools.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilesHelper {
	
	// 规范化目录路径用到的正则表达式
	private static final Pattern pattern = Pattern.compile("^(.+?)[/\\\\.]?[/\\\\.]?$");
	
	/**
	 * 以流的方式复制文件
	 *
	 * @param source 源文件流
	 * @param target 目标文件位置流
	 * @throws IOException 在文件读写时出现错误
	 */
	@SuppressWarnings("all")
	public static void copyFile (InputStream source, OutputStream target) throws IOException {
		byte[] tmp = new byte[source.available()];
		source.read(tmp);
		target.write(tmp);
	}
	
	/**
	 * 获取目录的绝对路径
	 *
	 * @param dir 目录的File对象
	 *
	 * @return 目录的带最后的斜线的绝对路径
	 */
	public static String getDirectoryAbsolutePath (File dir) {
		Matcher matcher = pattern.matcher(FilesHelper.encode(dir.getAbsolutePath()));
		if (matcher.find()) {
			return matcher.group(1) + File.separator;
		}
		return FilesHelper.encode(dir.getAbsolutePath());
	}
	
	/**
	 * 将传入的含有“%20”等字样的字符路径（或者不是路径也可以）重新编码为正常的内容
	 *
	 * @param path 未编码文本
	 * @return UTF-8 编码文本
	 */
	public static String encode(String path) {
		try {
			return java.net.URLDecoder.decode(path, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
}
