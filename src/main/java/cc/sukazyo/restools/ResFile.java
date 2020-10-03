package cc.sukazyo.restools;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;

/**
 * 资源包内的资源文件
 */
public class ResFile {
	
	/** 标明此文件隶属于哪个资源包 */
	private final ResourcesPackage resourcesPackage;
	/** 此文件的资源相对路径 */
	private final String path;
	
	/** 在DIR情况下的资源文件 */
	private final File dirFile;
	/** 在JAR下的资源文件目标 */
	private final JarEntry jarEntry;
	
	/**
	 * 标准资源文件获取方案
	 * 通过一个资源相对路径和项目源来获取一个资源文件
	 *
	 * @param resPack 项目源
	 * @param path 资源相对路径（相对于项目资源目录的路径）
	 * @throws IOException 读入文件时发生错误
	 */
	public ResFile (ResourcesPackage resPack, String path) throws IOException {
		JarEntry jarTemp = null;
		File dirTemp = null;
		resourcesPackage = resPack;
		this.path = path.substring(resPack.getResRootPath().length() - 1);
		if (resourcesPackage.isDirPack()) {
			dirTemp = new File(FilesHelper.encode(resourcesPackage.getRoot().getPath()) + "/" + path);
			if (!dirTemp.isFile())
				throw new IOException("No such resource in projDir: " + path);
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
		dirFile = dirTemp;
	}
	
	/**
	 * 从一个File对象获取一个资源文件对象
	 * 用于ResDir的列举
	 *
	 * @param resPack 项目源
	 * @param node 文件
	 */
	ResFile (ResourcesPackage resPack, File node) {
		jarEntry = null;
		resourcesPackage = resPack;
		dirFile = node;
		// TODO Path is not relative
		path = resPack.pathRelativization(FilesHelper.encode(node.getAbsolutePath()));
	}
	
	/**
	 * 从一个JarEntry获取一个文件
	 * 用于ResFile的列举
	 *
	 * @param resPack 源项目
	 * @param node Jar包内文件对象
	 */
	ResFile (ResourcesPackage resPack, JarEntry node) {
		jarEntry = node;
		resourcesPackage = resPack;
		dirFile = null;
		// TODO Path is not relative
		path = node.getName().substring(resPack.getResRootPath().length() - 1);
	}
	
	/**
	 * 获取相对于资源文件目录的此文件路径（资源相对路径）
	 *
	 * @return 资源相对路径
	 */
	public String getPath () {
		return path;
	}
	
	/**
	 * 获取这个文件属于的项目包
	 *
	 * @return 来源项目
	 */
	public ResourcesPackage getResourcesPackage() {
		return resourcesPackage;
	}
	
	/**
	 * 用于验证此文件是否为文件
	 * (好像并没有什么用)
	 *
	 * @return 是否为文件
	 */
	public boolean isFile () {
		if (resourcesPackage.getType() == ResourcesPackage.ProjectType.DIR) {
			return dirFile.isFile();
		} else {
			return !jarEntry.isDirectory();
		}
	}
	
	/**
	 * 获取文件读取流
	 *
	 * @return 文件流
	 * @throws IOException 读文件出现错误
	 */
	public InputStream read () throws IOException {
		if (resourcesPackage.getType() == ResourcesPackage.ProjectType.DIR) {
			return new FileInputStream(dirFile);
		} else {
			return resourcesPackage.getJar().getInputStream(jarEntry);
		}
	}
	
	/**
	 * 获取纯文本文件的文本
	 * 请不要用于二进制文件
	 *
	 * @return 文本文件字符串
	 * @throws IOException 读文件出现错误
	 */
	public String readAsString () throws IOException {
		InputStream inputStream = this.read();
		
		String str = null;
		byte[] data = new byte[inputStream.available()];
		if (inputStream.read(data) > -1) {
			str = new String(data);
		}
//		StringBuilder sb = new StringBuilder();
//		String line;
//
//		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//		while ((line = br.readLine()) != null) {
//			sb.append(line);
//		}
		return str;
	}
	
}
