package server;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;


public class ServerThread extends Thread 
{
    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;
    private String usuario;
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
            while(true) {
                String outputString = input.readLine();
                if(usuario==null) 
                {
                	usuario=outputString;
                }

                if(outputString.contains("/lista usuarios")) 
                {
                	StringBuilder stringBuilder=new StringBuilder();
                	for (int i = 0; i < threadList.size(); i++) {
                		System.out.println("- "+threadList.get(i).getUsuario()+"-"+ threadList.get(i).getSocket().isClosed()+"-"+ threadList.get(i).getSocket().isConnected());
                		stringBuilder.append("- "+threadList.get(i).getUsuario());
					}
                	printToClient("imprimir lista de usuarios");
                	printToClient(stringBuilder.toString());
                }
                if(outputString.contains("/usuario ###")) {
                	printToClient("iniciar chat privado");
                }

                if(outputString.equals("exit")) 
                {
                	for (int i = 0; i < threadList.size(); i++) {
                		System.out.println("- "+threadList.get(i).getUsuario()+"-"+ usuario);
                		if(threadList.get(i).getUsuario()==usuario) 
                		{
                			System.out.println("desconectando a usuario " + usuario);
                			threadList.remove(i);
                		}
					}	
                	
                	break;
                }
                //printToALlClients(outputString);
                System.out.println("Server received " + outputString);

            }


        } catch (Exception e) {
            System.out.println("Error occured " +e.getStackTrace());
        }
    }

    private void printToALlClients(String outputString) {
        for( ServerThread sT: threadList) {
            sT.output.println(outputString);
        }
    }
    
    
    private void printToClient(String outputString) 
    {
    	output.println(outputString); 
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
