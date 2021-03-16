package multiclientScoketsTCPUDP;

import java.io.Serializable;

public class FileEvent implements Serializable {

	public FileEvent() {
	}

	private static final long serialVersionUID = 1L;

	private String destinationDirectory;
	private String sourceDirectory;
	private String filename;
	private String userTarget;
	private long fileSize;
	private byte[] fileData;
	private String status;
	private PathStatus path;

	
	public PathStatus getPath() {
		return path;
	}

	public void setPath(PathStatus path) {
		this.path = path;
	}

	public String getDestinationDirectory() {
		return destinationDirectory;
	}

	public void setDestinationDirectory(String destinationDirectory) {
		this.destinationDirectory = destinationDirectory;
	}

	public String getSourceDirectory() {
		return sourceDirectory;
	}

	public void setSourceDirectory(String sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public String getUserTarget() {
		return userTarget;
	}

	public void setUserTarget(String userTarget) {
		this.userTarget = userTarget;
	}

}