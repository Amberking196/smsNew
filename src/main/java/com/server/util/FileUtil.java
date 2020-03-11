package com.server.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将文件或是文件夹打包压缩成zip格式
 * 
 * @author ysc
 */
public class FileUtil {
	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

	public FileUtil() {
	};

	/**
	 * 创建ZIP文件
	 * 
	 * @param sourcePath
	 *            文件或文件夹路径
	 * @param zipPath
	 *            生成的zip文件存在路径（包括文件名）
	 */
	public static void createZip(String sourcePath, String zipPath) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipPath);
			zos = new ZipOutputStream(fos);
			writeZip(new File(sourcePath), "", zos);
		} catch (FileNotFoundException e) {
			log.error("创建ZIP文件失败", e);
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (IOException e) {
				log.error("创建ZIP文件失败", e);
			}

		}
	}

	private static void writeZip(File file, String parentPath,
			ZipOutputStream zos) {
		if (file.exists()) {
			if (file.isDirectory()) {// 处理文件夹
				parentPath += file.getName() + File.separator;
				File[] files = file.listFiles();
				// 遍历夹打包
				for (File f : files) {
					writeZip(f, parentPath, zos);
				}
			} else {
				FileInputStream fis = null;
				DataInputStream dis = null;
				try {
					fis = new FileInputStream(file);
					dis = new DataInputStream(new BufferedInputStream(fis));
					ZipEntry ze = new ZipEntry(parentPath + file.getName());
					zos.putNextEntry(ze);
					byte[] content = new byte[1024];
					int len;
					while ((len = fis.read(content)) != -1) {
						zos.write(content, 0, len);
						zos.flush();
					}
				} catch (FileNotFoundException e) {
					log.error("创建ZIP文件失败", e);
				} catch (IOException e) {
					log.error("创建ZIP文件失败", e);
				} finally {
					try {
						if (dis != null) {
							dis.close();
						}
					} catch (IOException e) {
						log.error("创建ZIP文件失败", e);
					}
				}
			}
		}
	}

	static String substr = "";

	/**
	 * 创建ZIP文件 根据文件列表
	 * 
	 * @param sourcePath
	 *            文件或文件夹路径
	 * @param zipPath
	 *            生成的zip文件存在路径（包括文件名）
	 */
	public static void createZipByPath(String sourcePath, String zipPath,
			File[] file) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipPath);
			zos = new ZipOutputStream(fos);
			writeZipByPath(new File(sourcePath), "", zos, file);
		} catch (FileNotFoundException e) {
			log.error("创建ZIP文件失败", e);
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (IOException e) {
				log.error("创建ZIP文件失败", e);
			}

		}
	}

	private static void writeZipByPath(File file, String parentPath,
			ZipOutputStream zos, File[] filelist) {
		if (file.exists()) {
			if (file.isDirectory()) {// 处理文件夹
				parentPath += file.getName() + File.separator;
				// File [] files=file.listFiles();
				File[] filetemp = new File[filelist.length - 1];
				// 遍历文件
				for (int i = 0; i < filelist.length - 1; i++) {
					filetemp[i] = filelist[i];
				}
				for (File f : filetemp) {
					writeZipByPath(f, parentPath, zos, filelist);
				}
				/*
				 * for(File f:files){ writeZip(f, parentPath, zos,filelist); }
				 */
			} else {
				FileInputStream fis = null;
				DataInputStream dis = null;
				try {
					fis = new FileInputStream(file);
					dis = new DataInputStream(new BufferedInputStream(fis));
					System.out
							.println("===file.getName()====" + file.getName());
					System.out.println("===parentPath====" + parentPath);
					System.out
							.println("===file.getAbsolutePath().indexOf(parentPath)===="
									+ file.getAbsolutePath()
											.indexOf(parentPath));
					substr = file.getAbsolutePath().substring(
							file.getAbsolutePath().indexOf(parentPath)
									+ parentPath.length(),
							file.getAbsolutePath().indexOf(file.getName()));
					System.out.println("substr=="
							+ file.getAbsolutePath().substring(
									file.getAbsolutePath().indexOf(parentPath)
											+ parentPath.length(),
									file.getAbsolutePath().indexOf(
											file.getName())));
					ZipEntry ze = new ZipEntry(parentPath + substr
							+ file.getName());
					// ZipEntry ze = new ZipEntry(parentPath+"img\\" +
					// file.getName());
					zos.putNextEntry(ze);
					byte[] content = new byte[1024];
					int len;
					while ((len = fis.read(content)) != -1) {
						zos.write(content, 0, len);
						zos.flush();
					}
					System.out.println("====out");
				} catch (FileNotFoundException e) {
					log.error("创建ZIP文件失败", e);
				} catch (IOException e) {
					log.error("创建ZIP文件失败", e);
				} finally {
					try {
						if (dis != null) {
							dis.close();
						}
					} catch (IOException e) {
						log.error("创建ZIP文件失败", e);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		// FileUtil.createZip("D:\\workSpace5\\mrm\\target\\platform\\upms-bootstrap",
		// "D:\\workSpace5\\mrm\\target\\platform\\getzip\\upms-bootstrap.zip");
		// FileUtil.createZip("D:\\workspaces\\netbeans\\APDPlat\\APDPlat_Web\\target\\APDPlat_Web-2.2\\platform\\index.jsp",
		// "D:\\workspaces\\netbeans\\APDPlat\\APDPlat_Web\\target\\APDPlat_Web-2.2\\platform\\index.zip");

	}
}