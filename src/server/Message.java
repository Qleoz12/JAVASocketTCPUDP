package server;

public class Message {
	
	private String alias;
	private String message;
	private String user;
	private String userTarget;
	
	
	
	public Message() {
		// TODO Auto-generated constructor stub
	}

	
	public String getUserTarget() {
		return userTarget;
	}


	public void setUserTarget(String userTarget) {
		this.userTarget = userTarget;
	}


	public String getAlias() {
		return alias;
	}


	public void setAlias(String alias) {
		this.alias = alias;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}
	
	

}
