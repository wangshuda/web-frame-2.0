package com.cintel.frame.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cintel.frame.ftp.EasyFtpClient;
import com.cintel.frame.util.DateUtils;
import com.cintel.frame.util.FileUtils;
import com.cintel.frame.util.MyAntPathMatcher;
import com.cintel.frame.util.StringUtils;

public class UploadService implements IUploadService {
    private final static String _URL_SEPERATOR = "/";

    @SuppressWarnings("unused")
    private Log log = LogFactory.getLog(this.getClass());

    private UploadConfig uploadConfig;

    // ~ Save and delete operate methods.

    public UploadConfig getUploadConfig() {
        return uploadConfig;
    }

    public void setUploadConfig(UploadConfig uploadConfig) {
        this.uploadConfig = uploadConfig;
    }

    /**
     * 
     */
    public String getSuffixedFileName(String fileName) {
        String newFileName = fileName;
        if (StringUtils.hasText(uploadConfig.getFileSuffix())) {
            newFileName = fileName + uploadConfig.getFileSuffix();
        }
        return newFileName;
    }

    /**
     * save
     * 
     * @param fileData
     * @param filePath
     * @param fileName
     * @throws Exception
     */
    public int save(byte[] fileData, String filePath, String unSuffixedFileName) {
        String fileName = getSuffixedFileName(unSuffixedFileName);
        //
        int saveOk = 1;
        try {
            // Save in the local disk.
            if (this.uploadConfig.isSaveLocalFlag()) {
                File file = new File(this.uploadConfig.getFileHome() + filePath + fileName);
                FileUtils.save(fileData, file);
                //
                if (this.uploadConfig.isBackupFlag()) {
                    File wavBackupFile = new File(this.uploadConfig.getBackupFileHome() + filePath + fileName);
                    FileUtils.save(fileData, wavBackupFile);
                }
            }
            // Upload to the ftp server.
            if (this.uploadConfig.isUseFtpFlag()) {
                EasyFtpClient easyFtpClient = new EasyFtpClient(this.uploadConfig.getFtpServerIp(), this.uploadConfig
                        .getFtpServerPort());
                easyFtpClient.login(this.uploadConfig.getFtpUserName(), this.uploadConfig.getFtpUserPassword());
                //
                String fileLocation = this.uploadConfig.getFtpFileHome() + filePath;
                easyFtpClient.mkdirs(fileLocation); // Attention: the directory
                // of the ftp has changed.
                easyFtpClient.upload(fileData, fileName);
                //
                if (this.uploadConfig.isFtpBackupFlag()) { // the ftp also need
                    // do backup.
                    // firstly go back to the home directory. because the upload
                    // action previously
                    easyFtpClient.cdUp(fileLocation);
                    //
                    String backupFileHome = this.uploadConfig.getFtpBackupFileHome() + filePath;
                    easyFtpClient.mkdirs(backupFileHome);
                    easyFtpClient.upload(fileData, fileName);
                }
                //
                easyFtpClient.closeServer();
            }
        }
        catch (Exception ex) {
            log.error("Save failed!", ex);
            saveOk = 0;
        }
        return saveOk;
    }

    /**
     * save
     * 
     * @param fileData
     * @param fileName
     * @throws Exception
     */
    public int save(byte[] fileData, String fileName) {
        return this.save(fileData, "", fileName);
    }

    /**
     * delete
     * 
     * @param fileName
     * @throws Exception
     */
    public int delete(String fileName) {
        return this.delete("", fileName);
    }

    /**
     * delete
     * 
     * @param filePath
     * @param unSuffixedFileName
     * @throws Exception
     */
    public int delete(String filePath, String unSuffixedFileName) {
        String fileName = getSuffixedFileName(unSuffixedFileName);

        int deleteOk = 1;
        try {
            String fileLocation = null;
            // Delete from Local disk
            if (this.uploadConfig.isSaveLocalFlag()) {
                fileLocation = this.uploadConfig.getFileHome() + filePath + fileName;
                File file = new File(fileLocation);

                FileUtils.delete(file);
                //
                log.debug("Delete original file: " + fileLocation);
                //
                if (this.uploadConfig.isBackupFlag()) {
                    fileLocation = this.uploadConfig.getBackupFileHome() + filePath + fileName;
                    File backupFile = new File(fileLocation);
                    FileUtils.delete(backupFile);
                    //
                    log.debug("Delete backup file : " + fileLocation);
                }
            }
            // Delete from ftp server
            if (this.uploadConfig.isUseFtpFlag()) {
                EasyFtpClient easyFtpClient = new EasyFtpClient(this.uploadConfig.getFtpServerIp(), this.uploadConfig
                        .getFtpServerPort());
                easyFtpClient.login(this.uploadConfig.getFtpUserName(), this.uploadConfig.getFtpUserPassword());
                //
                fileLocation = this.uploadConfig.getFtpFileHome() + filePath;
                easyFtpClient.delete(fileLocation, fileName);

                log.debug("Delete original file from ftp : " + fileLocation);

                if (this.uploadConfig.isFtpBackupFlag()) {
                    easyFtpClient.cdUp(fileLocation);
                    //
                    easyFtpClient.delete(this.uploadConfig.getFtpBackupFileHome() + filePath, fileName);
                    //
                    log.debug("Delete backup file from ftp : " + fileLocation);
                }
                easyFtpClient.closeServer();
            }
        }
        catch (Exception ex) {
            log.error("delete failed!", ex);
            deleteOk = 0;
        }
        return deleteOk;
    }

    /**
     * get
     * 
     * @param fileName
     * @return
     * @throws Exception
     */
    public InputStream get(String fileName) {
        return this.get("", fileName);
    }

    /**
     * get
     * 
     * @param filePath
     * @param unSuffixedFileName
     * @return
     * @throws Exception
     */
    public InputStream get(String filePath, String unSuffixedFileName) {
        InputStream inputstream = null;

        try {
            String fileLocation = null;
            String fileName = getSuffixedFileName(unSuffixedFileName);
            // get from Local disk
            if (this.uploadConfig.isSaveLocalFlag()) {
                fileLocation = this.uploadConfig.getFileHome() + filePath + fileName;
                File file = new File(fileLocation);
                if (file.exists()) {
                    inputstream = new FileInputStream(file);
                }
                else {
                    log.warn("Can not find original file: " + fileLocation);
                }
                //
                if (inputstream == null && this.uploadConfig.isBackupFlag()) {
                    fileLocation = this.uploadConfig.getBackupFileHome() + filePath + fileName;
                    File backupFile = new File(fileLocation);
                    if (file.exists()) {
                        inputstream = new FileInputStream(backupFile);
                    }
                    else {
                        log.warn("Can not find backup file: " + fileLocation);
                    }
                }
            }

            // get from ftp server
            if (inputstream == null && this.uploadConfig.isUseFtpFlag()) {
                EasyFtpClient easyFtpClient = new EasyFtpClient(this.uploadConfig.getFtpServerIp(), this.uploadConfig
                        .getFtpServerPort());
                easyFtpClient.login(this.uploadConfig.getFtpUserName(), this.uploadConfig.getFtpUserPassword());
                //
                fileLocation = this.uploadConfig.getFtpFileHome() + filePath + fileName;
                inputstream = easyFtpClient.downLoad(fileLocation);

                if (inputstream == null && this.uploadConfig.isFtpBackupFlag()) {
                    easyFtpClient.cdUp(fileLocation);
                    //
                    inputstream = easyFtpClient
                            .downLoad(this.uploadConfig.getFtpBackupFileHome() + filePath + fileName);
                }
                easyFtpClient.closeServer();
            }
        }
        catch (Exception ex) {
            log.error("Get inputstream error!", ex);
        }
        return inputstream;
    }

    public boolean isExist(String fileName) {
        if (this.uploadConfig.isSaveLocalFlag()) {
            String fileLocation = this.uploadConfig.getFileHome() + fileName;
            File file = new File(fileLocation);
            return file.exists();
        }
        return false;
    }

    public void createDir(String filePath) {
        // TODO Auto-generated method stub
    }

    public ResourceInfo findResourceInfo(String filePath, String fileName) {
        // TODO Auto-generated method stub
        return null;
    }

    public ResourceInfo findResourceInfo(String fileName) {
        // TODO Auto-generated method stub
        return null;
    }

    public ResourceInfo[] listResourceInfo(String filePath) {
        return this.listResourceInfo(filePath, new String[] { "**" });
    }

    public void rename(String filePath, String fileName, String newFileName) {
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("rename {0} {1} to {2}.", filePath, fileName, newFileName));
        }
        //
        String oldFileURL = this.getTargetFileURL(filePath, fileName, false);
        String newFileURL = this.getTargetFileURL(filePath, newFileName, false);
        File file = new File(oldFileURL);
        file.renameTo(new File(newFileURL));
    }

    public String range(String filePath, String fileName, int startIndex, int endIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    public int save(InputStream in, String filePath, String fileName) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int save(InputStream in, String fileName) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void get(String fileName, OutputStream out) {
        this.get(null, fileName, out);
    }

    public void get(String filePath, String fileName, OutputStream out) {
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("get with filePath:{0}, fileName:{1}", filePath, fileName));
        }
        String targetFileURL = getTargetFileURL(filePath, fileName, false);
        if (log.isDebugEnabled()) {
            log.debug("Get file at : " + targetFileURL);
        }
        this.getByURL(targetFileURL, out);

    }

    public boolean isExistDir(String targetPathURL) {
        if (this.uploadConfig.isSaveLocalFlag()) {
            File file = new File(targetPathURL);
            return file.exists();
        }
        return false;
    }

    public ResourceInfo[] listResourceInfo(String pathLocation, String[] includeNamesPattern) {
        String targetPathURL = this.getTargetFileURL(pathLocation, null, true);

        if (log.isDebugEnabled()) {
            log.debug("listResourceInfo:" + targetPathURL);
        }

        File dirFile = new File(targetPathURL);
        if (!dirFile.exists()) {
            log.warn("can not find:" + targetPathURL);
            return new ResourceInfo[0];
        }
        //
        boolean canBeList = false;
        List<ResourceInfo> resultList = new ArrayList<ResourceInfo>();
        if (dirFile.isDirectory()) {
            File files[] = dirFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    canBeList = this.canBeList(file.getName(), includeNamesPattern);
                    if (canBeList) {
                        ResourceInfo resourceInfo = this.getResourceInfo(file);
                        resourceInfo.setPathLocation(pathLocation);
                        resultList.add(resourceInfo);
                    }
                }
            }
        }

        ResourceInfo[] subResourceInfoArr = resultList.toArray(new ResourceInfo[resultList.size()]);
        this.sort(subResourceInfoArr);
        //
        return subResourceInfoArr;

    }
    
	public ResourceInfo[] sort(ResourceInfo[] resourceInfos) {
		ResourceInfo temp;
		if (resourceInfos != null && resourceInfos.length > 0) {
			for (int i = 0; i < resourceInfos.length; i++) {
				for (int j = i + 1; j < resourceInfos.length; j++) {
					String createdTimeI = resourceInfos[i].getCreatedDateTime();
					String createdTimeJ = resourceInfos[j].getCreatedDateTime();
					if (StringUtils.hasText(createdTimeI) && StringUtils.hasText(createdTimeJ)) {						
						if (Long.parseLong(createdTimeI) < Long.parseLong(createdTimeJ)) {
							temp = resourceInfos[i];
							resourceInfos[i] = resourceInfos[j];
							resourceInfos[j] = temp;
						}
					}
				}
			}
		}
		//
		return resourceInfos;
	}

    /**
     * Depends the webdav in slide.
     */
    private ResourceInfo getResourceInfo(File file) {
        ResourceInfo resourceInfo = new ResourceInfo();
        // resourceInfo.setContentType(file.lastModified().gwebDavResource.getGetContentType());
        resourceInfo.setName(file.getName());
        resourceInfo.setSize(file.length());
        resourceInfo.setLastModified(DateUtils.getDate14FromDate(new Date(file.lastModified())));
        resourceInfo.setTargetURL(file.getPath());
        resourceInfo.setDirectory(file.isDirectory());
        return resourceInfo;
    }

    private boolean canBeList(String fileName, String[] includeNamesPattern) {
        boolean result = false;
        MyAntPathMatcher antPath = new MyAntPathMatcher();
        for (String namePattern : includeNamesPattern) {
            if (antPath.matchIgnoreCase(namePattern, fileName)) {
                return true;
            }
        }
        return result;
    }

    private String organizeFileSeperator(String filePath) {
        return filePath.replace("/", _URL_SEPERATOR);
    }

    public String getTargetFileURL(String filePath, String fileName, boolean endWithUrlSeperator) {
        StringBuffer buffer = new StringBuffer();

        boolean filePathIsEmpty = !StringUtils.hasText(filePath);
        String targetURL = this.uploadConfig.getFileHome();
        // if the filePath started with the targetURL, means the file path is an
        // file URL
        if (filePathIsEmpty || !filePath.startsWith(targetURL)) {
            buffer.append(targetURL);
        }
        //
        if (!filePathIsEmpty) {
            buffer.append(this.organizeFileSeperator(filePath));
            if (!_URL_SEPERATOR.equals("" + buffer.charAt(buffer.length() - 1))) {
                buffer.append(_URL_SEPERATOR);
            }
        }
        //
        if (StringUtils.hasText(fileName)) {
            if (!_URL_SEPERATOR.equals("" + buffer.charAt(buffer.length() - 1))) {
                buffer.append(_URL_SEPERATOR);
            }
            //
            buffer.append(fileName);
        }
        //
        if (!endWithUrlSeperator && _URL_SEPERATOR.equals("" + buffer.charAt(buffer.length() - 1))) {
            buffer.substring(0, buffer.length() - 1);
        }
        //
        return buffer.toString();
    }

    public int save(InputStream in, String filePath, String fileName, long size) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void getByURL(String targetFileURL, OutputStream out, boolean noClose) {
        File file = new File(targetFileURL);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            FileUtils.copy(fis, out, noClose);
        }
        catch (Exception e) {
            log.warn("Could not read file", e);
        }
        finally {
            try {
                fis.close();
            }
            catch (IOException e) {
                log.warn("Could not close InputStream", e);
            }
        }

    }
    
    public void getByURL(String targetFileURL, OutputStream out) {
        this.getByURL(targetFileURL, out, true);
    }
    
    public long loadSourceSize(String targetFileURL) {
        long rtnSize = -1;
        //
        File file = new File(targetFileURL);
        if(file != null) {
            rtnSize = file.length();
        }
        return rtnSize;
    }

    public ResourceInfo loadResourceCtx(String targetFileURL) {
        ResourceInfo resourceCtx = new ResourceInfo();
        //
        File file = new File(targetFileURL);
        if(file != null) {
            resourceCtx.setName(file.getName());
            resourceCtx.setSize(file.length());
        }
        //
        return resourceCtx;
    }

}
