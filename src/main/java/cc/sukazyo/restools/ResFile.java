package cc.sukazyo.restools;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;

public class ResFile {
	
	private final ResourcesPackage resourcesPackage;
	private final String path;
	
	private final File dirFile;
	private final JarEntry jarEntry;
	
	protected ResFile (ResourcesPackage resPack, String path) throws IOException {
		JarEntry jarTemp = null;
		File dirTemp = null;
		resourcesPackage = resPack;
		this.path = path.substring(resPack.getResRootPath().length() - 1);
		if (resourcesPackage.isDirPack()) {
			dirTemp = new File(resourcesPackage.getRoot().getPath() + "/" + path);
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
	
	protected ResFile (ResourcesPackage resPack, File node) {
		jarEntry = null;
		resourcesPackage = resPack;
		dirFile = node;
		// TODO Path is not relative
		path = resPack.pathRelativization(node.getAbsolutePath());
	}
	
	protected ResFile (ResourcesPackage resPack, JarEntry node) {
		jarEntry = node;
		resourcesPackage = resPack;
		dirFile = null;
		// TODO Path is not relative
		path = node.getName().substring(resPack.getResRootPath().length() - 1);
	}
	
	public String getPath () {
		return path;
	}
	
	public ResourcesPackage getResourcesPackage() {
		return resourcesPackage;
	}
	
	public boolean isFile () {
		if (resourcesPackage.getType() == ResourcesPackage.ProjectType.DIR) {
			return dirFile.isFile();
		} else {
			return !jarEntry.isDirectory();
		}
	}
	
	public InputStream read () throws IOException {
		if (resourcesPackage.getType() == ResourcesPackage.ProjectType.DIR) {
			return new FileInputStream(dirFile);
		} else {
			return resourcesPackage.getJar().getInputStream(jarEntry);
		}
	}
	
	public String readAsString () throws IOException {
		InputStream inputStream = this.read();
		
		StringBuilder sb = new StringBuilder();
		String line;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
	
}
