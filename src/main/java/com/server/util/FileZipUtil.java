package com.server.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理文件打包成ZIP的工具类 <br/>
 * 创建时间：2015-12-29
 * 
 * @author zhujun
 */
public class FileZipUtil {
	public FileZipUtil() {
	}

	/**
	 * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
	 * 
	 * @param sourceFilePath
	 *            :待压缩的文件路径
	 * @param zipFilePath
	 *            :压缩后存放路径
	 * @param fileName
	 *            :压缩后文件的名称
	 * @return
	 */
	public static boolean fileToZip(String sourceFilePath, String zipFilePath,
			String fileName) {
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		if (sourceFile.exists() == false) {
			System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
		} else {
			try {
				File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
				if (zipFile.exists()) {
					System.out.println(zipFilePath + "目录下存在名字为:" + fileName
							+ ".zip" + "打包文件.");
				} else {
					File[] sourceFiles = sourceFile.listFiles();
					if (null == sourceFiles || sourceFiles.length < 1) {
						System.out.println("待压缩的文件目录：" + sourceFilePath
								+ "里面不存在文件，无需压缩.");
					} else {
						fos = new FileOutputStream(zipFile);
						zos = new ZipOutputStream(new BufferedOutputStream(fos));
						byte[] bufs = new byte[1024 * 10];
						for (int i = 0; i < sourceFiles.length; i++) {
							// 创建ZIP实体，并添加进压缩包
							ZipEntry zipEntry = new ZipEntry(
									sourceFiles[i].getName());
							zos.putNextEntry(zipEntry);
							// 读取待压缩的文件并写进压缩包里
							fis = new FileInputStream(sourceFiles[i]);
							bis = new BufferedInputStream(fis, 1024 * 10);
							int read = 0;
							while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
								zos.write(bufs, 0, read);
							}
						}
						flag = true;
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				// 关闭流
				try {
					if (null != bis)
						bis.close();
					if (null != zos)
						zos.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return flag;
	}

	/**
	 * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
	 * 
	 * @param sourceFilePathlist
	 *            :待压缩的文件路径列表
	 * @param zipFilePath
	 *            :压缩后存放路径
	 * @param fileName
	 *            :压缩后文件的名称
	 * @return
	 */
	public static boolean fileToZipByPath(List<String> sourceFilePathlist,
			String zipFilePath, String fileName) {
		boolean flag = false;
		System.out.println("文件打包11111");
		// File sourceFile = new File(sourceFilePathlist.get(i));
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		if (sourceFilePathlist == null || sourceFilePathlist.size() == 0) {
			System.out.println("待压缩的文件目录：" + sourceFilePathlist + "不存在.");
		} else {
			try {
				File zipFile = new File(zipFilePath + "/" + fileName + ".zip");

				if (zipFile.exists()) {
					System.out.println(zipFilePath + "目录下存在名字为:" + fileName
							+ ".zip" + "打包文件.");
				} else {
					File[] sourceFiles = null;
					// File[] sourceFiles = sourceFile.listFiles();
					// 遍历待压缩的文件路径列表
					System.out.println("文件打包===" + sourceFilePathlist.get(0));
					/*
					 * for( int i = 0; i <= sourceFilePathlist.size();i++){
					 * sourceFiles[i] = new File(sourceFilePathlist.get(i)); }
					 */

					// (null == sourceFiles || sourceFiles.length<1){
					// System.out.println("待压缩的文件目录：" + sourceFilePathlist +
					// "里面不存在文件，无需压缩.");
					// }else{
					fos = new FileOutputStream(zipFile);
					zos = new ZipOutputStream(new BufferedOutputStream(fos));
					byte[] bufs = new byte[1024 * 10];
					for (int i = 0; i < sourceFilePathlist.size(); i++) {
						// 创建ZIP实体，并添加进压缩包
						// ZipEntry zipEntry = new
						// ZipEntry(sourceFiles[i].getName());
						ZipEntry zipEntry = new ZipEntry(
								sourceFilePathlist.get(i));
						zos.putNextEntry(zipEntry);
						// 读取待压缩的文件并写进压缩包里
						// fis = new FileInputStream(sourceFiles[i]);
						fis = new FileInputStream(new File(
								sourceFilePathlist.get(i)));
						bis = new BufferedInputStream(fis, 1024 * 10);
						int read = 0;
						while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
							zos.write(bufs, 0, read);
						}
					}
					flag = true;
					// }
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				// 关闭流
				try {
					if (null != bis)
						bis.close();
					if (null != zos)
						zos.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return flag;
	}

	/*
	 * public static void main(String[] args){ String sourceFilePath =
	 * "F:\\test"; String zipFilePath = "F:\\testzip"; String fileName = "test";
	 * boolean flag = FileZipUtil.fileToZip(sourceFilePath, zipFilePath,
	 * fileName); if(flag){ System.out.println("文件打包成功!"); }else{
	 * System.out.println("文件打包失败!"); } }
	 */
	private static final Logger log = LoggerFactory
			.getLogger(FileZipUtil.class);

	private static void writeZip(File file, String parentPath,
			ZipOutputStream zos, File[] filelist) {
		if (file.exists()) {
			if (file.isDirectory()) {// 处理文件夹
				parentPath += file.getName() + File.separator;
				File[] files = file.listFiles();
				File[] filetemp = new File[filelist.length + 1];
				for (int i = 0; i < filelist.length; i++) {
					filetemp[i] = filelist[i];
				}
				for (File f : filetemp) {
					writeZip(f, parentPath, zos, filelist);
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
					// ZipEntry ze = new ZipEntry(parentPath + file.getName());
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

	public static void main(String[] args) {
		List<String> str = new ArrayList<String>();
		str.add("F:\\test\\1.txt");// {"F:\\test\\1.txt","F:\\test\\2.txt"};
		str.add("F:\\test\\2.txt");
		// sourceFilePathlist[0] = "F:\\test\\1.txt";
		String zipFilePath = "F:\\testzip";
		String fileName = "test2";
		System.out.println("文件打包成功222222");
		boolean flag = FileZipUtil.fileToZipByPath(str, zipFilePath, fileName);
		if (flag) {
			System.out.println("文件打包成功!");
		} else {
			System.out.println("文件打包失败!");
		}
	}
}
