package cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import multiclientScoketsTCPUDP.ClienteUDP;
import multiclientScoketsTCPUDP.HandlerUdp;
import multiclientScoketsTCPUDP.PathStatus;
import server.HandlerTCP;

public class Cliente {


	
	public static void main(String[] args) {
		try (Socket socket = new Socket("25.85.105.161", 5000)) {
			// reading the input from server
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// returning the output to the server : true statement is to flush the buffer
			// otherwise
			// we have to do it manuallyy
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

			// taking the user input
			Scanner scanner = new Scanner(System.in);
			String userInput;
			String response;
			String clientName = "empty";

			ClientRunnable clientRun = new ClientRunnable(socket);

			Thread hilorecepcion = new Thread(clientRun);
			hilorecepcion.start();
			
			ClienteUDP clienteUDP=new ClienteUDP();
			clienteUDP.createConnection();
			
			HandlerUdp handlerUdp=new HandlerUdp();
			handlerUdp.start();
			// loop closes when user enters exit command

			do {

				if (clientName.equals("empty")) {
					System.out.println("Enter your name ");
					userInput = scanner.nextLine();
					clientName = userInput;
					output.println(userInput);
					if (userInput.equals("/exit")) {
						break;
						
					}
				} else {
					String message = ("(me)" + " : ");
					System.out.print(message);
					userInput = scanner.nextLine();
					output.println("(" + clientName + ")" + " : " + userInput);
					if (userInput.equalsIgnoreCase("/exit"))
					{
						System.out.print("adios! " + clientName);
						hilorecepcion.interrupt();
						handlerUdp.interrupt();
						output.close();
						socket.close();
						break;
					}
					
					if (userInput.contains("/send"))
					{
						clienteUDP.setIpAddres("25.85.105.161");
						clienteUDP.setPath(PathStatus.toServer);
						String[] piecies=userInput.split("\\s+");
						if(piecies.length==3)
						{
							clienteUDP.sendFile(System.getenv("SystemDrive")+"/chat/send/"+piecies[2], System.getenv("SystemDrive")+"/chat/receive",piecies[1]);
						}
						
						if(piecies.length==2)
						{
							clienteUDP.sendFile(System.getenv("SystemDrive")+"/chat/send/"+piecies[1], System.getenv("SystemDrive")+"/chat/receive");
						}
						
						
						
					}
					
					
				}

			} while (!userInput.equals("/exit"));

			
			System.exit(0);
		} catch (Exception e) {
			System.out.println("Exception occured in client main: " + e.getStackTrace());
			e.printStackTrace();
		}
	}


}