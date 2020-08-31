package cc.sukazyo.restools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;

public class ResDir {

	private final ResourcesPackage resourcesPackage;
	private final String path;

	private final File dir;
	private final JarEntry jarEntry;

	protected ResDir (ResourcesPackage resPack, String path) throws IOException {
		JarEntry jarTemp = null;
		File dirTemp = null;
		resourcesPackage = resPack;
		this.path = path.substring(resPack.getResRootPath().length() - 1);
		if (resourcesPackage.isDirPack()) {
			dirTemp = new File(resourcesPackage.getRoot().getPath() + "/" + path);
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
	
	protected ResDir (ResourcesPackage resPack, File node) {
		jarEntry = null;
		resourcesPackage = resPack;
		dir = node;
		path = resPack.pathRelativization(node.getAbsolutePath());
	}
	
	protected ResDir (ResourcesPackage resPack, JarEntry node) {
		jarEntry = node;
		resourcesPackage = resPack;
		dir = null;
		// TODO Path is not relative
		path = node.getName().substring(resPack.getResRootPath().length() - 1);
	}
	
	public String getPath () {
		return path;
	}
	
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
	
	public void extract (File toDir) throws IOException {
		extract(toDir, true);
	}
	
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
			throw new IOException("Create Directory Failed: " + toDir.getAbsolutePath());
		}
	}
	
}
