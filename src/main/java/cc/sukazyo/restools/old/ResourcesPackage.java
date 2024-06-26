package cc.sukazyo.restools.old;

import cc.sukazyo.restools.utils.FilesHelper;
import cc.sukazyo.restools.utils.PathsHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarFile;

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
	
	/** The type of this resource package.
	 * <p>
	 * Maybe DIR or JAR.
	 *
	 * @since 0.2.x
	 */
	@Nonnull
	private final PackageType type;
	
	@Nonnull
	private final String resRoot;
	
	/** Root directory of the resource package.
	 *<p>
	 * Only exists when the {@link #type} is {@link PackageType#DIR}
	 *
	 * @since 0.2.x
	 */
	@Nullable
	private File root;
	/** When this project is not packed, the root directory of the resource. */
	@Nullable
	private JarFile jar;
	
	/**
	 * 以标准的方案获取到项目资源索引
	 *
	 * @param proj 某项目包含的类，ResPack将以此类所在的位置寻找资源
	 * @param resRoot 资源文件的文件夹相对于项目根目录的位置
	 */
	public ResourcesPackage(Class<?> proj, String resRoot) {
		
		resRoot = PathsHelper.cutPath(resRoot);
		
		String protocol = proj.getResource("").getProtocol();
		if ("jar".equals(protocol)){
			try {
				jar = new JarFile(
						java.net.URLDecoder.decode(
								proj.getProtectionDomain().getCodeSource().getLocation().getPath(),
								StandardCharsets.UTF_8.name()
						)
				);
			} catch (IOException e) {
				e.printStackTrace();
			}
			type = PackageType.JAR;
		} else if("file".equals(protocol)){
			root = new File(proj.getResource("/" + resRoot).getFile()).getParentFile();
			type = PackageType.DIR;
		} else {
			System.err.println("Crashed! Unknown File Protocol.");
			System.exit(-123);
			type = null;
		}
		
		this.resRoot = resRoot;
		
	}
	
	/**
	 * 获得项目当前的文件结构类型
	 * @return 项目文件类型
	 */
	public PackageType getType () {
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
		return type == PackageType.DIR;
	}
	
	/**
	 * 检查项目是否为打包后的项目
	 *
	 * @return 是否已打包
	 */
	public boolean isJarPack () {
		return type == PackageType.JAR;
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
		path = PathsHelper.cutPath(path);
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
				return "dir:" + FilesHelper.encode(root.getAbsoluteFile().getAbsolutePath());
		} else if (jar != null) {
			return "jar:" + jar.getName();
		}
		return null;
	}
	
	/**
	 * 用于将绝对路径（以"/"(Unix)或盘符(Win)为根的路径）截取为以项目资源文件为根的相对路径
	 *
	 * @param absolutePath 绝对路径
	 * @return 相对路径
	 */
	@SuppressWarnings("all")
	public String pathRelativization (String absolutePath) {
		if (this.isDirPack()) {
			String assRoot = FilesHelper.encode(this.root.getAbsolutePath()) + File.separator + resRoot.substring(0, resRoot.length()-1);
//			System.out.println(absolutePath + "|" + assRoot);
			if (!absolutePath.substring(0, assRoot.length()).equals(assRoot))
				return absolutePath;
			return absolutePath.substring(assRoot.length()).replaceAll("\\\\", "/");
		} else {
			return null;
		}
	}
	
}
