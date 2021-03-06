package server;

public class Command {

    private String message;
    private final String userInput;
    private boolean isCommand = false;
    private boolean roomChange = false;
    private boolean isDM = false;
    private boolean isInfo = false;
    private boolean isExit=false;
    private HandlerTCP server;
    
    public final String USERS="/users";
	
    
    public Command(String userInput, HandlerTCP handlerTCP) {
        this.userInput = userInput;
        this.server = handlerTCP;
        this.execute();
    }

    private void execute() {
        // If userinput starts with slash
        if (this.userInput.startsWith("/")) {
            this.isCommand = true;
            switch (this.userInput.split(" ")[0]) {
                case "/dm":
                    this.isDM = true;
                    break;
                case USERS:
                    this.message = "Current online users:";
                    for (ServerThread user : this.server.getThreadList() )
                    {	
                    	if(user.getHandlerTCP().getAlias()!=server.getAlias()) 
                    	{
                    		this.message += "\n\t" + user.getHandlerTCP().getIp()+"\t"+user.getHandlerTCP().getAlias();
                    	}
                    }
                    break;
                case "/exit":
                    this.isExit = true;
                    break;
                case "/room":
                    this.roomChange = true;
                    break;
                case "/help":
                case "/info":
                    this.isInfo= true;
                	this.message = "Commands:";
                    this.message += "\n\t/users";
                    //this.message += "\n\t/room {room_name}";
                    this.message += "\n\t/help";
                    this.message += "\n\t/dm {user_name} {message}";
                    break;
                default:
                    this.message = "Command does not exists.";
                    break;
            }
        } else {
            this.isCommand = false;
        }
    }

    public boolean isCommand() {
        return this.isCommand;
    }

    public boolean isDM() {
        return this.isDM;
    }

    public boolean isInfo() {
        return this.isInfo;
    }
    
    public boolean isExit() {
        return this.isExit;
    }
    public boolean roomChange() {
        return this.roomChange;
    }

    public String getMessage() {
        return this.message;
    }
    
    
    
    
}
