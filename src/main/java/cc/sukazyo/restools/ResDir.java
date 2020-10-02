package cc.sukazyo.restools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;

/**
 * 资源包内的资源文件夹
 */
public class ResDir {
	
	/** 标明此文件夹隶属于哪个资源包 */
	private final ResourcesPackage resourcesPackage;
	/** 此文件夹的资源相对路径 */
	private final String path;
	
	/** 在DIR下的资源文件夹目标 */
	private final File dir;
	/** 在JAR下的资源文件夹目标 */
	private final JarEntry jarEntry;
	
	/**
	 * 标准资源文件夹获取方案
	 * 通过一个资源相对路径和项目源来获取一个资源文件夹
	 *
	 * @param resPack 项目源
	 * @param path 资源相对路径（相对于项目资源目录的路径）
	 * @throws IOException 读入文件时发生错误
	 */
	protected ResDir (ResourcesPackage resPack, String path) throws IOException {
		JarEntry jarTemp = null;
		File dirTemp = null;
		resourcesPackage = resPack;
		this.path = path.substring(resPack.getResRootPath().length() - 1);
		if (resourcesPackage.isDirPack()) {
			dirTemp = new File(FilesHelper.encode(resourcesPackage.getRoot().getPath()) + "/" + path);
			if (!dirTemp.isDirectory())
				throw new IOException("No such resource dir in projDir: " + path);
		} else if (resourcesPackage.isJarPack()) {
			boolean getFile = false;
			for (Enumeration<JarEntry> e = resourcesPackage.getJar().entries(); e.hasMoreElements(); ) {
				JarEntry entry = e.nextElement();
				if (entry.getName().equals(path)) {
					jarTemp = entry;
					getFile = true;
					break;
				}
			}
			if (!getFile)
				throw new IOException("Resource not found in project jar : " + path);
		} else {
			throw new IOException("Project type is not standard.");
		}
		jarEntry = jarTemp;
		dir = dirTemp;
	}
	
	/**
	 * 从一个File对象获取一个资源文件夹对象
	 * 用于ResDir的列举
	 *
	 * @param resPack 项目源
	 * @param node 系统层文件
	 */
	protected ResDir (ResourcesPackage resPack, File node) {
		jarEntry = null;
		resourcesPackage = resPack;
		dir = node;
		path = resPack.pathRelativization(node.getAbsolutePath());
	}
	
	/**
	 * 从一个JarEntry获取一个文件夹
	 * 用于ResFile的列举
	 *
	 * @param resPack 源项目
	 * @param node Jar包内文件对象
	 */
	protected ResDir (ResourcesPackage resPack, JarEntry node) {
		jarEntry = node;
		resourcesPackage = resPack;
		dir = null;
		// TODO Path is not relative
		path = node.getName().substring(resPack.getResRootPath().length() - 1);
	}
	
	/**
	 * 获取相对于资源文件目录的此文件夹路径（资源相对路径）
	 *
	 * @return 资源相对路径
	 */
	public String getPath () {
		return path;
	}
	
	/**
	 * 获取这个文件夹属于的项目包
	 *
	 * @return 来源项目
	 */
	public ResourcesPackage getResPack () { return resourcesPackage; }
	
	/**
	 * 获取此文件夹下的资源文件
	 * 仅获取直属于此目录下的文件，此目录的子目录和子目录文件均不获取
	 *
	 * @return 资源文件组
	 */
	@SuppressWarnings("all")
	public ResFile[] listFiles () {
		List<ResFile> rt = new ArrayList<>();
		if (resourcesPackage.isJarPack()) {
			for (Enumeration<JarEntry> e = resourcesPackage.getJar().entries(); e.hasMoreElements(); ) {
				JarEntry entry = e.nextElement();
				if (
						entry.getName().length() > jarEntry.getName().length() &&
						entry.getName().substring(0, jarEntry.getName().length()).equals(jarEntry.getName()) &&
						!(
								entry.getName().substring(jarEntry.getName().length()).contains("/") ||
								entry.getName().substring(jarEntry.getName().length()).contains("\\")
						)
				) {
					rt.add(new ResFile(resourcesPackage, entry));
				}
			}
		} else {
			for (File file : Objects.requireNonNull(dir.listFiles())) {
				if (file.isFile()) {
					rt.add(new ResFile(resourcesPackage, file));
				}
			}
		}
		return rt.toArray(new ResFile[0]);
	}
	
	/**
	 * 获取当前文件夹下的子文件夹
	 * 和 listFiles 一样，只获取直属于此文件夹的文件夹，不获取子文件夹的子文件夹
	 *
	 * @return 子文件夹组
	 */
	@SuppressWarnings("all")
	public ResDir[] listDirs () {
		List<ResDir> rt = new ArrayList<>();
		if (resourcesPackage.isJarPack()) {
			for (Enumeration<JarEntry> e = resourcesPackage.getJar().entries(); e.hasMoreElements(); ) {
				JarEntry entry = e.nextElement();
				if (
						entry.getName().length() > jarEntry.getName().length() &&
								entry.getName().substring(0, jarEntry.getName().length()).equals(jarEntry.getName()) &&
								entry.getName().matches(".+?[\\\\/]$")
				) {
					rt.add(new ResDir(resourcesPackage, entry));
				}
			}
		} else {
			for (File file : Objects.requireNonNull(dir.listFiles())) {
				if (file.isDirectory()) {
					rt.add(new ResDir(resourcesPackage, file));
				}
			}
		}
		return rt.toArray(new ResDir[0]);
	}
	
	/**
	 * 将此文件夹下的所有内容递归解压输出到目录中
	 * 包括子文件，子文件夹，子文件夹下的子文件和子文件夹 and so on...
	 * 如果目标目录不存在则自动创建目录
	 * 会覆盖已有文件
	 *
	 * @param toDir 目标文件夹
	 * @throws IOException 输出目标不是一个文件夹或访问失败
	 */
	public void extract (File toDir) throws IOException {
		extract(toDir, true);
	}
	
	/**
	 * 将此文件夹下的所有内容递归解压输出到目录中
	 * 包括子文件，子文件夹，子文件夹下的子文件和子文件夹 and so on...
	 * 如果目标目录不存在则自动创建目录
	 *
	 * @param toDir 目标文件夹
	 * @param overwrite 是否覆盖已存在的文件
	 * @throws IOException 输出目标不是一个文件夹或访问失败
	 */
	public void extract (File toDir, boolean overwrite) throws IOException {
		if (toDir.isDirectory() || toDir.mkdir()) {
			for (ResFile file : this.listFiles()) {
				File to = new File(
						FilesHelper.getDirectoryAbsolutePath(toDir) +
								file.getPath().substring(this.path.length()));
				if (!overwrite && to.isFile()) continue;
				FilesHelper.copyFile(file.read(), new FileOutputStream(to));
			}
			for (ResDir resDir : this.listDirs()) {
				resDir.extract(new File(
						FilesHelper.getDirectoryAbsolutePath(toDir) +
								resDir.getPath().substring(this.path.length())),
						overwrite
				);
			}
		} else {
			throw new IOException("Create Directory Failed: " + FilesHelper.encode(toDir.getAbsolutePath()));
		}
	}
	
}
