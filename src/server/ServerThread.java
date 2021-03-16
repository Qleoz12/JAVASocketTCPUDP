package server;


import java.util.ArrayList;

import multiclientScoketsTCPUDP.HandlerUdp;


public class ServerThread extends Thread 
{
    private ArrayList<ServerThread> threadList;

    
    private HandlerUdp handlerUdp;
    private HandlerTCP handlerTCP;
    


    public ServerThread(HandlerUdp handlerUdp, HandlerTCP handlerTCP) 
    {
		this.handlerUdp = handlerUdp;
		this.handlerTCP = handlerTCP;
	}



	@Override
    public void run() {
        try {
            
        	handlerUdp.start();
        	handlerTCP.start();

        } catch (Exception e) {
            System.out.println("Error occured " +e.getStackTrace());
        }
    }



	public ArrayList<ServerThread> getThreadList() {
		return threadList;
	}



	public void setThreadList(ArrayList<ServerThread> threadList) {
		this.threadList = threadList;
	}



	public HandlerUdp getHandlerUdp() {
		return handlerUdp;
	}



	public void setHandlerUdp(HandlerUdp handlerUdp) {
		this.handlerUdp = handlerUdp;
	}



	public HandlerTCP getHandlerTCP() {
		return handlerTCP;
	}



	public void setHandlerTCP(HandlerTCP handlerTCP) {
		this.handlerTCP = handlerTCP;
	}

	
	


    
}
