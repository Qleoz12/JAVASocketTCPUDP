package server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class ServerThread extends Thread 
{
    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;
    private String usuario;
    private String alias;
    private String ip;
    private Integer code;
    
    public ServerThread(Socket socket, ArrayList<ServerThread> threads) {
        this.socket = socket;
        this.threadList = threads;
    }

    @Override
    public void run() {
        try {
            
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            //esta cosa es la stream de salida para  cadacliente 
            output = new PrintWriter(socket.getOutputStream(),true);


            //inifite loop for server
            while(socket.isConnected())
            {
                String outputString = input.readLine();
                if(outputString==null) 
                {
                	break;
                }
                //(leo) message :  hola
                Message message=new Message();
                
                String[] piecies=outputString.split("\\s+");
                if(alias!=null) 
                {
                	String alias=piecies[0];
                    String messagedata=piecies[2];
                    message.setAlias(alias);
                    message.setMessage(messagedata);
                    message.setUser(usuario);
                    
                    Command command = new Command(message.getMessage(), this);
                   
                    //valor de lista de usuario
                    if(command.isCommand() 
                    	&& message.getMessage().equalsIgnoreCase(command.USERS)) 
                    {
                    	printToClient(command.getMessage());
                    }
                    if(command.isCommand() 
                        	&& command.isDM()) 
                    {
                    	if(piecies.length>2) message.setUserTarget(piecies[3]);
                    	message.setAlias(piecies[0].replace("(","").replace(")",""));
                    	String mensaje=String.join(" ",Arrays.copyOfRange(piecies, 4, piecies.length));
                        message.setMessage(mensaje);
                        message.setUser(usuario);
                    	mensajeTocliente(message);
                    }
                    if(command.isCommand() 
                        	&& command.isInfo()) 
                    {
                    	printToClient(command.getMessage());
                    }
                    
                    if(command.isCommand() 
                    	&& command.isExit()) 
                    {
                    	for (int i = 0; i < threadList.size(); i++)
                    	{
                    		System.out.println("- "+threadList.get(i).getAlias()+"-"+ alias);
                    		if(threadList.get(i).getAlias()==alias) 
                    		{
                    			System.out.println("desconectando a usuario " + usuario+" "+alias);
                    			threadList.remove(i);
                    		}
    					}	
                    	
                    	break;
                    }
                    
                    if(!command.isCommand()) printToALlClients(outputString);
                }
                
                if(alias==null) 
                {	
                	boolean flagalias=checkAlias(outputString);
                	if(flagalias) alias=outputString;
                	if(!flagalias) printToClient("alias ya en uso, registrese con otro usuario");
                }
                
                
                
                System.out.println("Server received " + outputString);

            }
            System.out.println("desconexion de socket " + usuario);


        } catch (Exception e) {
            System.out.println("Error occured " +e.getStackTrace());
        }
    }
    
    
    private void mensajeTocliente(Message message)
    {
    	if (message==null) System.out.println("error de mensaje privado");
    	if (message==null) return;
    	    	
		for (int i = 0; i < threadList.size(); i++)
    	{
    		if(threadList.get(i).getAlias().equalsIgnoreCase(message.getUserTarget())) 
    		{
    			String dmMessage="("+message.getAlias()+") : "+message.getMessage();
    			threadList.get(i).output.println(dmMessage);
    		}
		}
	}

	public InetAddress getip(Socket socket) 
    {
    	SocketAddress socketAddress = socket.getRemoteSocketAddress();
    	
    	if (socketAddress instanceof InetSocketAddress) {
    	    InetAddress inetAddress = ((InetSocketAddress)socketAddress).getAddress();
    	    if (inetAddress instanceof Inet4Address)
    	        //System.out.println("IPv4: " + inetAddress);
    	    	return inetAddress;
    	    else if (inetAddress instanceof Inet6Address)
    	        //System.out.println("IPv6: " + inetAddress);
    	    	return inetAddress;
    	    else
    	        System.err.println("Not an IP address.");
    	    	return null;
    	} else {
    	    System.err.println("Not an internet protocol socket.");
    	    return null;
    	}
    }
    
	
	private boolean checkAlias(String alias)
    {
    	
		for (int i = 0; i < threadList.size(); i++)
    	{
    		if(threadList.get(i).getAlias()!=null
    			&& threadList.get(i).getAlias().equalsIgnoreCase(alias))
    		{
    			return false;
    		}	
		}
    	
    	return true;
    }
	
	
	
    public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	private void printToALlClients(String outputString)
    {
        for( ServerThread sT: threadList) {
            if(!sT.getAlias().equalsIgnoreCase(alias))
            {	
            	sT.output.println(outputString);
            }
        }
    }
    
    private void printToClient(String outputString) 
    {
    	output.println(outputString); 
    }

    
    
    
    public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ArrayList<ServerThread> getThreadList() {
		return threadList;
	}

	public void setThreadList(ArrayList<ServerThread> threadList) {
		this.threadList = threadList;
	}

	public PrintWriter getOutput() {
		return output;
	}

	public void setOutput(PrintWriter output) {
		this.output = output;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
    
    
}
