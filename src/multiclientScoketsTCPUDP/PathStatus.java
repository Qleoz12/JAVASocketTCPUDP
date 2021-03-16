package multiclientScoketsTCPUDP;

public enum PathStatus {
	
	toClient("toclient"),
	toServer("toServer");
	
	
	
	
	private PathStatus(String path) {
		this.path = path;
	}

	String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
}
