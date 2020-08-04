package cc.sukazyo.restools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ResourcesPackage {
	
	private final ProjectType type;
	
	private File root;
	private JarFile jar;
	
	public ResourcesPackage(String resRoot) throws IOException {
		
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
			root = new File(ResourcesPackage.class.getResource("/" + resRoot).getFile());
			type = ProjectType.DIR;
		} else {
			throw new IOException("Project File is not on a dir or in a jar.");
		}
		
	}
	
	public InputStream getResourceStream (String path) throws IOException {
		
		if (path.charAt(0) == '\\' || path.charAt(0) == '/') {
			path = path.substring(1);
		}
		
		if (type == ProjectType.DIR) {
			return new FileInputStream(new File(root.getPath() + "/" + path));
		} else if (type == ProjectType.JAR) {
			for (Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements(); ) {
				JarEntry entry = e.nextElement();
				if (entry.getName().contains(path)) {
					return jar.getInputStream(entry);
				}
			}
		}
		throw new IOException("File not found in project : " + path);
		
	}
	
//	p	if (type == ProjectType.DIR) {
//		System.out.println(new File(root.getPath() + "/" + path).getPath());
//	} else if (type == ProjectType.JAR) {
//		System.out.println("Hi");
//	}
//}ublic void test (String path) {
	
	
	@Override
	public String toString() {
		if (root != null) {
			return "dir:" + root.getAbsoluteFile();
		} else if (jar != null) {
			return "jar:" + jar.getName();
		}
		return null;
	}
	
	private enum ProjectType {
		DIR, JAR
	}
	
}
