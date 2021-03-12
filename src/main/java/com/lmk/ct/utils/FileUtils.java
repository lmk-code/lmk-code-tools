package com.lmk.ct.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.output.FileWriterWithEncoding;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件的工具类
 * 
 * @author LaoMake
 * @since 1.0
 *
 */
public class FileUtils {

	/** 日志记录器 */
	private static Logger log = LoggerFactory.getLogger(FileUtils.class);

	/** 缓冲数组的大小 */
	public static final int BUFFER_SIZE = 1024;

	/** 默认字符集 */
	public static final String CHAR_SET = "UTF-8";

	/**
	 * 获取当前应用的ClassPath根目录
	 * 
	 * @author LaoMake
	 * @since 1.0
	 * @param clazz
	 * @return
	 */
	public static String getRootClassPath(Class<?> clazz) {
		String rootClassPath = null;
		ClassLoader classLoader = clazz.getClassLoader();
		try {
			rootClassPath = classLoader.getResource("").toURI().getPath();
			rootClassPath = new File(rootClassPath).getAbsolutePath();
		} catch (URISyntaxException e) {
			rootClassPath = classLoader.getResource("").getPath();
			rootClassPath = new File(rootClassPath).getAbsolutePath();
		}

		return rootClassPath;
	}

	/**
	 * 获取当前应用的WebContent根目录
	 * 
	 * @author LaoMake
	 * @since 1.0
	 * @param clazz
	 * @return
	 */
	public static String getWebRootPath(Class<?> clazz) {
		String webRootPath = null;
		try {
			webRootPath = clazz.getResource("/").toURI().getPath();
			webRootPath = new File(webRootPath).getParentFile().getParentFile().getCanonicalPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return webRootPath;
	}

	@SuppressWarnings("rawtypes")
	public static String getPath(Class clazz) {
		String path = clazz.getResource("").getPath();
		return new File(path).getAbsolutePath();
	}

	public static String getPath(Object object) {
		String path = object.getClass().getResource("").getPath();
		return new File(path).getAbsolutePath();
	}

	public static String getPackagePath(Object object) {
		Package p = object.getClass().getPackage();
		return p != null ? p.getName().replaceAll("\\.", "/") : "";
	}

	public static File getFileFromJar(String file) {
		throw new RuntimeException("Not finish. Do not use this method.");
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @author LaoMake
	 * @since 1.0
	 *
	 * @param filePath
	 * @return
	 */
	public static boolean exists(String filePath) {
		boolean exists = false;
		if (StringUtils.isNotBlank(filePath))
			exists = new File(filePath).exists();
		return exists;
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 */
	public static void delFile(String filePath) {
		File temp = new File(filePath);
		if (temp.exists()) {
			temp.delete();
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 */
	public static void delFolder(String folderPath) {
		try {
			clearFolder(folderPath); // 删除完里面所有内容

			String filePath = folderPath;
			File myFilePath = new File(filePath);
			if (myFilePath.exists()) {
				myFilePath.delete(); // 删除空文件夹
			}
		} catch (Exception e) {
			log.error("系统异常：\n" + e.getMessage());
		}
	}

	/**
	 * 清空文件夹
	 * 
	 * @param folderPath
	 * @return
	 */
	public static boolean clearFolder(String folderPath) {
		boolean flag = false;
		File file = new File(folderPath);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (folderPath.endsWith(File.separator)) {
				temp = new File(folderPath + tempList[i]);
			} else {
				temp = new File(folderPath + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				clearFolder(folderPath + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(folderPath + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 复制文件夹
	 * 
	 * @param sourceFolder
	 * @param targetFolder
	 */
	public static void copyFolder(File sourceFolder, File targetFolder) {
		if ((sourceFolder != null) && sourceFolder.exists()) {
			if (!targetFolder.exists())
				targetFolder.mkdirs();

			try {
				String[] files = sourceFolder.list();
				File sourceFile = null;
				File targetFile = null;
				for (int i = 0; i < files.length; i++) {
					sourceFile = new File(sourceFolder.getPath() + File.separator + files[i]);
					targetFile = new File(targetFolder.getPath() + File.separator + files[i]);
					if (!targetFile.exists())

						if (sourceFile.isFile()) {
							targetFile.createNewFile();
							copyFileByBinary(sourceFile, targetFile);
						} else {
							targetFile.mkdirs();
							copyFolder(sourceFile, targetFile);
						}

				}
			} catch (Exception e) {
				log.error("系统异常：\n" + e.getMessage());
			}
		} else {
			log.error("找不到相关文件：" + sourceFolder.getPath() + ",  " + targetFolder.getPath());
		}

	}

	/**
	 * 统计文件数量
	 * 
	 * @param folderPath
	 * @return
	 */
	public static int countFile(String folderPath) {
		int fileCount = 0;

		File file = new File(folderPath);
		if (file.exists() && file.isDirectory()) {
			String[] fileNameList = file.list();
			File temp = null;
			for (String tempName : fileNameList) {
				if (!folderPath.endsWith(File.separator))
					folderPath += File.separator;

				temp = new File(folderPath + tempName);

				if (temp.isFile())
					fileCount++;

				else if (temp.isDirectory())
					fileCount = fileCount + countFile(folderPath + tempName);
			}
		}
		return fileCount;
	}

	/**
	 * 统计文本行数
	 * 
	 * @param folderPath
	 * @return
	 */
	public static int countLine(String folderPath) {
		int lineCount = 0;

		File file = new File(folderPath);
		if (file.exists() && file.isDirectory()) {
			String[] fileNameList = file.list();
			File temp = null;
			for (String tempName : fileNameList) {
				if (!folderPath.endsWith(File.separator))
					folderPath += File.separator;

				temp = new File(folderPath + tempName);

				if (temp.isFile()) {
					LineNumberReader lnr = null;
					try {
						lnr = new LineNumberReader(new FileReader(temp));
						lnr.skip(Long.MAX_VALUE);
						lineCount += lnr.getLineNumber() + 1;
					} catch (FileNotFoundException e) {
						log.error("系统异常：\n" + e.getMessage());
					} catch (IOException e) {
						log.error("系统异常：\n" + e.getMessage());
					} finally {
						try {
							if (lnr != null)
								lnr.close();
						} catch (IOException e) {
							log.error("系统异常：\n" + e.getMessage());
						}
					}
				} else if (temp.isDirectory())
					lineCount += countLine(folderPath + tempName);
			}
		}
		return lineCount;
	}

	/**
	 * 输入流中读取文本（UTF-8编码）
	 * 
	 * @param inputStream
	 *            输入流
	 * @param closeAfterRead
	 *            读取完成后是否关闭输入流
	 * @return
	 */
	public static String readTextFromInputStream(InputStream inputStream, boolean closeAfterRead) {
		return readTextFromInputStream(inputStream, closeAfterRead, CHAR_SET);
	}

	/**
	 * 输入流中读取文本
	 * 
	 * @param is
	 *            输入流
	 * @param is
	 *            读取完成后是否关闭输入流
	 * @param charSet
	 *            文本字符集
	 * @return
	 */
	public static String readTextFromInputStream(InputStream is, boolean closeAfterRead, String charSet) {
		String content = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();

			int len = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();

			content = new String(baos.toByteArray(), charSet);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.close();
					baos = null;
				}
				if (closeAfterRead) {
					if (is != null)
						is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * 以字节数组形式读取文件
	 * 
	 * @param sourceFile
	 * @return
	 */
	public static byte[] readFileByBinary(File sourceFile) {
		byte[] result = null;
		if ((sourceFile != null) && sourceFile.exists()) {
			ByteArrayOutputStream baos = null;
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			
			try {
				baos = new ByteArrayOutputStream();
				fis = new FileInputStream(sourceFile);
				bis = new BufferedInputStream(fis);

				int len = -1;
				byte[] buffer = new byte[BUFFER_SIZE];
				while ((len = bis.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				baos.flush();

				result = baos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(baos != null) {
						baos.close();
						baos = null;
					}
					
					if (bis != null) {
						bis.close();
						bis = null;
					}
					if (fis != null) {
						fis.close();
						fis = null;
					}
				} catch (IOException e) {
					log.error("系统异常：\n" + e.getMessage());
				}
			}
		} else {
			log.error("找不到相关文件：" + sourceFile.getPath());
		}
		return result;
	}

	/**
	 * 以文本的形式读取文件（UTF-8编码）
	 * 
	 * @param sourceFile
	 * @return
	 */
	public static String readFileByText(File sourceFile) {
		return readFileByText(sourceFile, CHAR_SET);
	}

	/**
	 * 以文本的形式读取文件
	 * 
	 * @param sourceFile
	 * @param charSet
	 * @return
	 */
	public static String readFileByText(File sourceFile, String charSet) {
		String result = null;
		try {
			byte[] data = readFileByBinary(sourceFile);
			if ((data != null) && (data.length > 0))
				result = new String(data, charSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 按行读取文件（UTF-8编码）
	 * 
	 * @param sourceFile
	 * @return
	 */
	public static List<String> readFileByLines(File sourceFile) {
		return readFileByLines(sourceFile, CHAR_SET);
	}

	/**
	 * 按行读取文件
	 * 
	 * @param sourceFile
	 * @param charSet
	 * @return
	 */
	public static List<String> readFileByLines(File sourceFile, String charSet) {
		List<String> result = new ArrayList<>();
		if ((sourceFile != null) && sourceFile.exists()) {
			FileInputStream fis = null;

			LineNumberReader lnr = null;
			Reader reader = null;
			BufferedReader mBufferedReader = null;
			try {
				fis = new FileInputStream(sourceFile);

				lnr = new LineNumberReader(new FileReader(sourceFile));
				lnr.skip(Long.MAX_VALUE);
				reader = new InputStreamReader(fis, charSet);
				mBufferedReader = new BufferedReader(reader);

				String resultString = null;
				while ((resultString = mBufferedReader.readLine()) != null)
					result.add(resultString);

			} catch (Exception e) {
				log.error("系统异常：\n" + e.getMessage());
			} finally {
				try {
					if (fis != null) {
						fis.close();
						fis = null;
					}
					if (lnr != null) {
						lnr.close();
						lnr = null;
					}
					if (reader != null) {
						reader.close();
						reader = null;
					}
					if (mBufferedReader != null) {
						mBufferedReader.close();
						mBufferedReader = null;
					}

				} catch (Exception e) {
					log.error("系统异常：\n" + e.getMessage());
				}
			}
		} else {
			log.error("找不到相关文件：" + sourceFile.getPath());
		}

		return result;
	}

	/**
	 * 以二进制方式复制文件
	 * 
	 * @param sourceFile
	 * @param targetFile
	 */
	public static void copyFileByBinary(File sourceFile, File targetFile) {
		if (((sourceFile != null) && sourceFile.exists()) && ((targetFile != null) && targetFile.exists())) {
			FileInputStream fis = null;
			FileOutputStream fos = null;
			// 新建文件输出流并对它进行缓冲
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				fis = new FileInputStream(sourceFile);
				bis = new BufferedInputStream(fis);

				fos = new FileOutputStream(targetFile);
				bos = new BufferedOutputStream(fos);

				// 缓冲数组
				byte[] buffer = new byte[BUFFER_SIZE];
				int len;
				while ((len = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, len);
				}
				bos.flush();
			} catch (FileNotFoundException e) {
				log.error("系统异常：\n" + e.getMessage());
			} catch (IOException e) {
				log.error("系统异常：\n" + e.getMessage());
			} finally {
				try {
					if (bis != null) {
						bis.close();
						bis = null;
					}
					if (fis != null) {
						fis.close();
						fis = null;
					}
					if (bos != null) {
						bos.close();
						bos = null;
					}
					if (fos != null) {
						fos.close();
						fos = null;
					}
				} catch (IOException e) {
					log.error("系统异常：\n" + e.getMessage());
				}
			}
		} else {
			log.error("找不到相关文件：" + sourceFile.getPath() + ",  " + targetFile.getPath());
		}
	}



	/**
	 * 将字符串写入文件
	 *
	 * @param string
	 * @param targetFile
	 * @param isAppend
	 */
	public static void writeToFile(String string, File targetFile, Boolean isAppend) {
		writeToFile(string, targetFile, isAppend, CHAR_SET);
	}

	/**
	 * 将字符串写入文件
	 *
	 * @param string
	 * @param targetFile
	 * @param isAppend
	 * @param charSet
	 */
	public static void writeToFile(String string, File targetFile, Boolean isAppend, String charSet) {
		FileWriterWithEncoding writer = null;
		try {
			writer = new FileWriterWithEncoding(targetFile, charSet, isAppend);

			writer.write(string);
			writer.flush();
		} catch (IOException e) {
			log.error("系统异常：\n" + e.getMessage());
		} finally {
			if (writer != null) {
				try {
					writer.close();
					writer = null;
				} catch (IOException e) {
					log.error("系统异常：\n" + e.getMessage());
				}
			}
		}
	}

	/**
	 * 将字节数组写入到文件
	 * 
	 * @param data
	 * @param targetFile
	 */
	public static void writeToFile(byte[] data, File targetFile) {
		if (((data != null) && (data.length > 0)) && (targetFile != null)) {

			ByteArrayInputStream bis = null;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			try {
				bis = new ByteArrayInputStream(data);
				fos = new FileOutputStream(targetFile);
				bos = new BufferedOutputStream(fos);
				// 缓冲数组
				byte[] buffer = new byte[BUFFER_SIZE];
				int len;
				while ((len = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, len);
				}
				bos.flush();
			} catch (FileNotFoundException e) {
				log.error("系统异常：\n" + e.getMessage());
			} catch (IOException e) {
				log.error("系统异常：\n" + e.getMessage());
			} finally {
				try {
					if (bos != null) {
						bos.close();
						bos = null;
					}
					if (fos != null) {
						fos.close();
						fos = null;
					}

					if (bis != null) {
						bis.close();
						bis = null;
					}
				} catch (IOException e) {
					log.error("系统异常：\n" + e.getMessage());
				}
			}
		} else {
			log.error("参数错误");
		}
	}

	/**
	 * 将输入流的内容写入到文件
	 * 
	 * @param is
	 * @param targetFile
	 * @param isAppend
	 */
	public static void writeToFile(InputStream is, File targetFile, Boolean isAppend) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try {
			fos = new FileOutputStream(targetFile);
			bos = new BufferedOutputStream(fos);

			byte[] buffer = new byte[BUFFER_SIZE];
			int len;
			while ((len = is.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			bos.flush();
		} catch (Exception e) {
			log.error("系统异常：\n" + e.getMessage());
		} finally {
			try {
				if (bos != null) {
					bos.close();
					bos = null;
				}
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (Exception e) {
				log.error("系统异常：\n" + e.getMessage());
			}
		}
	}

	/**
	 * 确保文件夹存在，不存在则创建文件夹
	 * 
	 * @author LaoMake
	 * @date 2014年10月23日
	 * @param folderPath
	 */
	public static void makeSureFolderExits(String folderPath) {
		File folder = new File(folderPath);
		if (!folder.exists())
			folder.mkdirs();
	}

	/**
	 * 压缩目标文件，支持多级目录压缩
	 * 
	 * @param sourceFilePath
	 * @param targetFilePath
	 * @return
	 */
	public static boolean compressor(String sourceFilePath, String targetFilePath) {
		return compressor(sourceFilePath, targetFilePath, CHAR_SET);
	}

	/**
	 * 压缩目标文件，支持多级目录压缩
	 * 
	 * @param sourceFilePath
	 *            需要压缩的文件或目录
	 * @param targetFilePath
	 *            目标输出文件
	 * @param charSet
	 *            字符集
	 * @return 是否压缩成功
	 */
	public static boolean compressor(String sourceFilePath, String targetFilePath, String charSet) {
		boolean success = false;

		File sourceFile = new File(sourceFilePath);
		File targetFile = new File(targetFilePath);

		// 判断文件是否存在
		if (sourceFile.exists()) {
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			ZipOutputStream zos = null;

			try {
				// 定义输出文件
				fos = new FileOutputStream(targetFile);
				bos = new BufferedOutputStream(fos);
				zos = new ZipOutputStream(bos, Charset.forName(charSet));

				compressorFile(zos, null, sourceFile);

				zos.flush(); // 刷新缓存
				success = true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (zos != null) {
						zos.close();
						zos = null;
					}
					if (bos != null) {
						bos.close();
						bos = null;
					}
					if (fos != null) {
						fos.close();
						fos = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} else {
			log.info("目标文件不存在");
		}

		// 压缩失败则删除压缩文件
		if (!success && targetFile.exists())
			targetFile.delete();

		return success;
	}

	/**
	 * 添加单个文件
	 * 
	 * @param zos
	 * @param parentPath
	 * @param file
	 */
	private static void compressorFile(ZipOutputStream zos, String parentPath, File file) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		// 压缩单个文件
		try {
			if (file.isFile()) {
				// 单个文件
				ZipEntry entry = null;
				if (StringUtils.isBlank(parentPath))
					entry = new ZipEntry(file.getName());
				else
					entry = new ZipEntry(parentPath + "/" + file.getName());

				zos.putNextEntry(entry);

				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);

				byte[] buffer = new byte[BUFFER_SIZE];

				int len;
				while ((len = bis.read(buffer)) != -1) {
					zos.write(buffer, 0, len);
				}

				bis.close();
				fis.close();
				zos.closeEntry();
			} else {
				// 文件夹
				if (parentPath == null)
					parentPath = file.getName();
				else
					parentPath = parentPath + "/" + file.getName();

				File[] files = file.listFiles();
				for (File tempFile : files) {
					compressorFile(zos, parentPath, tempFile);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
					bis = null;
				}
				if (fis != null) {
					fis.close();
					fis = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
