package cc.sukazyo.restools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resources Package
 *
 * 项目资源文件的索引
 * 同时也是项目主类，定义了项目的元属性
 *
 * 此类及其包及其子包的所有类的作者均为：
 * @author Cookie Sukazyo Eyre
 */
public class ResourcesPackage {
	
	/**
	 * Sukazyo 的项目都会有一个 Package ID
	 * @value
	 */
	public static final String PACKID = "suk-restools";
	/**
	 * 当前的语义化版本号
	 * @value
	 */
	public static final String VERSION = "0.2";
	/**
	 * 当前的开发版本号，是开发者辨认版本的重要途径
	 * 每次完成一项小任务，提交一次commit都会更改，确保流出版本的build号不相同
	 * @value
	 * （enmnm...如果不是出了什么事故的话...）
	 */
	public static final int BUILD = 6;
	
	/** 标明这个项目的文件结构类型 */
	private final ProjectType type;
	
	/** 资源文件在项目内的位置，一般比如"assets" */
	private final String resRoot;
	
	/** 当项目是未打包的情况时的资源根目录 */
	private File root;
	/** 当项目打包后的jar文件对象 */
	private JarFile jar;
	
	/**
	 * 以标准的方案获取到项目资源索引
	 *
	 * @param proj 某项目包含的类，ResPack将以此类所在的位置寻找资源
	 * @param resRoot 资源文件的文件夹相对于项目根目录的位置
	 *
	 * @throws IOException 当读文件/文件夹出现问题时抛出的异常
	 */
	public ResourcesPackage(Class<?> proj, String resRoot) throws IOException {
		
		resRoot = cutPath(resRoot);
		
		String protocol = proj.getResource("").getProtocol();
		if ("jar".equals(protocol)){
			jar = new JarFile(
					java.net.URLDecoder.decode(
							proj.getProtectionDomain().getCodeSource().getLocation().getPath(),
							StandardCharsets.UTF_8.name()
					)
			);
			type = ProjectType.JAR;
		} else if("file".equals(protocol)){
			root = new File(proj.getResource("/" + resRoot).getFile()).getParentFile();
			type = ProjectType.DIR;
		} else {
			throw new IOException("Project File is not on a dir or in a jar.");
		}
		
		this.resRoot = resRoot;
		
	}
	
	/**
	 * 获得项目当前的文件结构类型
	 * @return 项目文件类型
	 */
	public ProjectType getType () {
		return type;
	}
	
	/**
	 * 用于获取到项目打包后的jar文件对象
	 * @return jar文件对象
	 */
	JarFile getJar () {
		return jar;
	}
	
	/**
	 * 用于获取到当项目未打包时的所在目录
	 * @return 所在目录
	 */
	File getRoot () {
		return root;
	}
	
	/**
	 * 获取定义的资源在项目内的储存位置
	 *
	 * @return 资源储存位置
	 */
	public String getResRootPath() {
		return resRoot;
	}
	
	/**
	 * 检查项目是否为打包前的项目
	 *
	 * @return 是否未打包
	 */
	public boolean isDirPack () {
		return type == ProjectType.DIR;
	}
	
	/**
	 * 检查项目是否为打包后的项目
	 *
	 * @return 是否已打包
	 */
	public boolean isJarPack () {
		return type == ProjectType.JAR;
	}
	
	/**
	 * 获取到一个资源文件
	 *
	 * @param path 以资源文件目录为根目录的路径
	 * @return 一个资源文件对象
	 * @throws IOException 读文件时出现错误
	 */
	public ResFile getResource (String path) throws IOException {
		if (path.charAt(0) == '\\' || path.charAt(0) == '/')
			path = path.substring(1);
		return new ResFile(this, resRoot + path);
	}
	
	/**
	 * 获取到一个资源目录
	 *
	 * @param path 以资源文件目录为根目录的路径
	 * @return 一个资源目录对象
	 * @throws IOException 寻找目录时出现错误
	 */
	public ResDir getResDir (String path) throws IOException {
		path = cutPath(path);
		return new ResDir(this, resRoot + path);
	}
	
	/**
	 * 返回说明这个项目的储存形式和路径的字符串
	 * eg: jar:/home/your/108/4434/ResourcesTools.jar
	 *
	 * @return 字符串
	 */
	@Override
	public String toString() {
		if (root != null) {
			return "dir:" + root.getAbsoluteFile();
		} else if (jar != null) {
			return "jar:" + jar.getName();
		}
		return null;
	}
	
	/**
	 * 项目的两种储存形式
	 * DIR: 以未打包的形式储存
	 * JAR: 以已打包的形式储存于一个jar文件中
	 */
	public enum ProjectType {
		DIR, JAR
	}
	
	/**
	 * 用于规范输入的目录路径
	 * 所有的路径都会被规范为 "xxx/"的形式（前不带斜杠，后带斜杠）
	 * 只能用于目录
	 *
	 * @param path 用户输入
	 * @return 规范好的路径
	 */
	private String cutPath (String path) {
		Matcher matcher = Pattern.compile("^[/\\\\]?(.+?)[/\\\\]?$").matcher(path);
		if (matcher.find()) {
			path = matcher.group(1) + "/";
		}
		return path;
	}
	
	/**
	 * 用于将绝对路径（以"/"(Unix)或盘符(Win)为根的路径）截取为以项目资源文件为根的相对路径
	 *
	 * @param absolutePath 绝对路径
	 * @return 相对路径
	 */
	public String pathRelativization (String absolutePath) {
		if (this.isDirPack()) {
			String assRoot = this.root.getAbsolutePath() + File.separator + resRoot.substring(0, resRoot.length()-1);
//			System.out.println(absolutePath + "|" + assRoot);
			if (!absolutePath.substring(0, assRoot.length()).equals(assRoot))
				return absolutePath;
			return absolutePath.substring(assRoot.length()).replaceAll("\\\\", "/");
		} else {
			return null;
		}
	}
	
}
