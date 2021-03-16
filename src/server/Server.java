package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import multiclientScoketsTCPUDP.HandlerUdp;

public class Server {

    public static void main(String[] args)
    {
        //using serversocket as argument to automatically close the socket
        //the port number is unique for each server

        //list to add all the clients thread
        ArrayList<ServerThread> threadList = new ArrayList<>();
        HandlerUdp handlerUdp=new HandlerUdp();
        handlerUdp.start();
        
        try (ServerSocket serversocket = new ServerSocket(5000)){
            while(true) {
                Socket socket = serversocket.accept();
                HandlerTCP handlerTCP=new HandlerTCP(socket, threadList);
                
                
                handlerTCP.setIp(handlerTCP.getip(socket).toString().replace("/", ""));
                handlerTCP.setUsuario(socket.getInetAddress().getHostName());
                handlerTCP.start();
               
                handlerUdp.setThreads(threadList);
                
                ServerThread serverThread = new ServerThread(handlerUdp,handlerTCP);
                threadList.add(serverThread); 
                //get all the list of currently running thread

            }
        } catch (Exception e) {
            System.out.println("Error occured in main: " + e.getStackTrace());
            e.printStackTrace();
        }
    }
}