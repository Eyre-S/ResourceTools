package cc.sukazyo.restools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourcesPackage {
	
	public static final String PACKID = "suk-restools";
	public static final String VERSION = "0.1-dev";
	public static final int BUILD = 2;
	
	private final ProjectType type;
	
	private final String resRoot;
	
	private File root;
	private JarFile jar;
	
	public ResourcesPackage(String resRoot) throws IOException {
		
		resRoot = cutPath(resRoot);
		
		String protocol = ResourcesPackage.class.getResource("").getProtocol();
		if ("jar".equals(protocol)){
			jar = new JarFile(
					java.net.URLDecoder.decode(
							this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(),
							StandardCharsets.UTF_8.name()
					)
			);
			type = ProjectType.JAR;
		} else if("file".equals(protocol)){
			root = new File(ResourcesPackage.class.getResource("/" + resRoot).getFile()).getParentFile();
			type = ProjectType.DIR;
		} else {
			throw new IOException("Project File is not on a dir or in a jar.");
		}
		
		this.resRoot = resRoot;
		
	}
	
	public ProjectType getType () {
		return type;
	}
	
	public JarFile getJar () {
		return jar;
	}
	
	public File getRoot () {
		return root;
	}
	
	public boolean isDirPack () {
		return type == ProjectType.DIR;
	}
	
	public boolean isJarPack () {
		return type == ProjectType.JAR;
	}
	
	public ResFile getResource (String path) throws IOException {
		if (path.charAt(0) == '\\' || path.charAt(0) == '/')
			path = path.substring(1);
		return new ResFile(this, resRoot + path);
	}
	
	public ResDir getResDir (String path) throws IOException {
		path = cutPath(path);
		return new ResDir(this, resRoot + path);
	}
	
	@Override
	public String toString() {
		if (root != null) {
			return "dir:" + root.getAbsoluteFile();
		} else if (jar != null) {
			return "jar:" + jar.getName();
		}
		return null;
	}
	
	enum ProjectType {
		DIR, JAR
	}
	
	public void test () {
		if (isJarPack()) {
			for (Enumeration<JarEntry> e = getJar().entries(); e.hasMoreElements(); ) {
				JarEntry entry = e.nextElement();
				System.out.println(entry.getName());
			}
		} else if (isDirPack()) {
			forEachDir(root);
		}
	}
	
	private static void forEachDir (File dir) {
		for (File sub : dir.listFiles()) {
			if (sub.isFile()) {
				System.out.println(sub.getPath());
			} else if (sub.isDirectory()) {
				forEachDir(sub);
			}
		}
	}
	
	private String cutPath (String path) {
		Matcher matcher = Pattern.compile("^[\\/\\\\]?(\\S+?)[\\/\\\\]?$").matcher(path);
		if (matcher.find()) {
			path = matcher.group(1) + "/";
		}
		return path;
	}
	
}
