package com.cintel.frame.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @author wangshuda
 *
 * see org.springframework.util.FileCopyUtils
 */
public class FileUtils {
	private static Log log = LogFactory.getLog(FileUtils.class);
	//
	public static final int BUFFER_SIZE = 4096;

	public static File locate(File start, String pattern) {
		if(start == null) {
			return null;
		}
		else {
			MyAntPathMatcher pathMatcher = new MyAntPathMatcher();
			String fileAbsolutePath = null;
			if(start.isDirectory()){
				File files[] = start.listFiles();
				for(File fileInTheDir:files) {
					if(fileInTheDir.isDirectory()) {
						File file = locate(fileInTheDir, pattern);
						if(file != null) {
							return file;
						}
					}
					else {
						//fileInTheDir is a file
						fileAbsolutePath = StringUtils.cleanPath(fileInTheDir.getAbsolutePath());
						if(pathMatcher.matchIgnoreCase(pattern, fileAbsolutePath)) {
							return fileInTheDir;
						}
					}
				}
			}
			else {
				//start is a file
				fileAbsolutePath =  StringUtils.cleanPath(start.getAbsolutePath());
				if(pathMatcher.matchIgnoreCase(pattern, fileAbsolutePath)) {
					return start;
				}
			}
			return null;
		}
	}
	/**
	 * 
	 * @method: find
	 * @return: List<File>
	 * @author: WangShuDa
	 * @param start
	 * @param pattern : ant url pattern using * or **
	 * @return: if start is null, return Collections.EMPTY_LIST.
	 */
	@SuppressWarnings("unchecked")
	public static List<File> find(File start, String pattern) {
		if(start == null) {
			return Collections.EMPTY_LIST;
		}
		else {
			MyAntPathMatcher pathMatcher = new MyAntPathMatcher();
			String fileAbsolutePath = null;
			List<File> result = new ArrayList<File>();
			if(start.isDirectory()){
				File files[] = start.listFiles();
				for(File fileInTheDir:files) {
					if(fileInTheDir.isDirectory()) {
						List<File> allItsSubFilesList = find(fileInTheDir, pattern);
						result.addAll(allItsSubFilesList);
					}
					else {
						//fileInTheDir is a file
						fileAbsolutePath = StringUtils.cleanPath(fileInTheDir.getAbsolutePath());
						if(pathMatcher.matchIgnoreCase(pattern, fileAbsolutePath)) {
							result.add(fileInTheDir);
						}
					}
				}
			}
			else {
				//start is a file
				fileAbsolutePath =  StringUtils.cleanPath(start.getAbsolutePath());
				if(pathMatcher.matchIgnoreCase(pattern, fileAbsolutePath)) {
					result.add(start);
				}
			}
			return result;
		}
	}
	
	public static void createDir(String dirPath) throws Exception {
		File file = new File(dirPath);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
	public static void save(InputStream in, File out) throws IOException {
		//
		File parent = out.getParentFile();
		if(!parent.exists()) {
			parent.mkdirs();
		}
		copy(in, new FileOutputStream(out));
	}
	
	public static void save(byte[] in, File out) throws IOException {
		save(new ByteArrayInputStream(in), out);
	}
	
	public static void delete(File file) throws IOException {
		//
		file.delete();
	}
	
	public static boolean delete(File file,boolean isForce)
	{
		if(file.exists()){
			file.delete();
			return true;
		}
		else{
			return false;
		}
	}
	//---------------------------------------------------------------------
	// Copy methods for java.io.File
	//---------------------------------------------------------------------
	
	/**
	 * 
	 */
	public static int copy(File in, File out) throws IOException {
		return copy(new BufferedInputStream(new FileInputStream(in)),
		    new BufferedOutputStream(new FileOutputStream(out)));
	}

	/**
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void copy(byte[] in, File out) throws IOException {
		ByteArrayInputStream inStream = new ByteArrayInputStream(in);
		OutputStream outStream = new BufferedOutputStream(new FileOutputStream(out));
		copy(inStream, outStream);
	}
	
	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] copyToByteArray(File in) throws IOException {
		return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
	}


	//---------------------------------------------------------------------
	// Copy methods for java.io.InputStream / java.io.OutputStream
	//---------------------------------------------------------------------

	/**
	 * 
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException {
        return copy(in, out, true);
	}
    
    public static int copy(InputStream in, OutputStream out, boolean autoClose) throws IOException {
        if(in == null) {
            return 0;
        }
        //
        try {
            int byteCount = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        }
        finally {
            try {
                in.close();
            }
            catch (Exception ex) {
                log.warn("Could not close InputStream :" + ex);
            }
            try {
                if(autoClose) {
                    out.close();  
                }
            }
            catch (Exception ex) {
                log.warn("Could not close OutputStream" + ex);                
            }
        }
    }
	/**
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void copy(byte[] in, OutputStream out) throws IOException {
		try {
			out.write(in);
		}
		finally {
			try {
				out.close();
			}
			catch (Exception ex) {
				log.warn("Could not close OutputStream" + ex);
			}
		}
	}
	
	/**
	 * 
	 * @param in
	 * @return byte[] 
	 * @throws IOException
	 */
	public static byte[] copyToByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
		copy(in, out);
		return out.toByteArray();
	}


	//---------------------------------------------------------------------
	// Copy methods for java.io.Reader / java.io.Writer
	//---------------------------------------------------------------------

	/**
	 * 
	 */
	public static int copy(Reader in, Writer out) throws IOException {
		try {
			int byteCount = 0;
			char[] buffer = new char[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		}
		finally {
			try {
				in.close();
			}
			catch (Exception ex) {
				log.warn("Could not close Reader:" + ex);
			}
			try {
				out.close();
			}
			catch (Exception ex) {
				log.warn("Could not close Writer:" + ex);
			}
		}
	}

	public static void copy(String in, Writer out) throws IOException {
		try {
			out.write(in);
		}
		finally {
			try {
				out.close();
			}
			catch (Exception ex) {
				log.warn("Could not close Writer :" + ex);
			}
		}
	}

	public static String copyToString(Reader in) throws IOException {
		StringWriter out = new StringWriter();
		copy(in, out);
		return out.toString();
	}
	
	public static void main(String args[]) {
		File file = new File("D:\\ProgramFiles\\Java\\jakarta-tomcat-5.0.28\\webapps\\ipc-acr");
		
		List<File> allFilesList = FileUtils.find(file, "**/jdbc.properties");
		for(File fileInTheDir:allFilesList) {
			System.out.println(fileInTheDir.getAbsoluteFile());
		}
		//
		File locatedFile = FileUtils.locate(file, "**/jdbc.properties2");
		if(locatedFile != null) {
			System.out.println(locatedFile.getAbsoluteFile());
		}
		
	}
}
