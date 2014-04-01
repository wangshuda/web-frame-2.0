package com.cintel.frame.upload;



public class ResourceInfo {
	
	private boolean isDirectory;
	
	private String name;

	private String contentType;

	private long size;

	private String lastModified;
	
	private String createdDateTime;
	
	private String pathLocation;

	private String targetURL;
    
	public String getContentTypePrefix() {
		return (contentType == null || contentType.length() == 0) ? null :contentType.substring(0, contentType.indexOf('/'));
	}
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getPathLocation() {
		return pathLocation;
	}

	public void setPathLocation(String pathLocation) {
		this.pathLocation = pathLocation;
	}

	public String getTargetURL() {
		return targetURL;
	}

	public void setTargetURL(String targetURL) {
		this.targetURL = targetURL;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
}
