package multiclientScoketsTCPUDP;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import server.ServerThread;

public class HandlerUdp extends Thread
{

	private DatagramSocket socket = null;
	private FileEvent fileEvent = null;
	private ArrayList<ServerThread> threads;
	private ClienteUDP clienteUDP;
	
	public HandlerUdp() 
	{
	}


	@Override
	public void run() {
		this.createAndListenSocket();
	}


	public void createAndListenSocket() {
		try{
			socket = new DatagramSocket(9876);
			byte[] incomingData = new byte[1024 * 1000 * 50];
			while (true) {
				DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
				socket.receive(incomingPacket);
				byte[] data = incomingPacket.getData();
				ByteArrayInputStream in = new ByteArrayInputStream(data);
				ObjectInputStream is = new ObjectInputStream(in);
				fileEvent = (FileEvent) is.readObject();
				
				if (fileEvent.getStatus().equalsIgnoreCase("Error")) {
					System.out.println("Some issue happened while packing the data @ client side");
				}
				
				createAndWriteFile(); // writing the file to hard disk
				if(fileEvent.getPath()!=null && fileEvent.getPath().equals(PathStatus.toServer))
				{
					processFile();
				}
				InetAddress IPAddress = incomingPacket.getAddress();
				int port = incomingPacket.getPort();
				String reply = "Thank you for the message";
				byte[] replyBytea = reply.getBytes();
				DatagramPacket replyPacket = new DatagramPacket(replyBytea, replyBytea.length, IPAddress, port);
				socket.send(replyPacket);
			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void processFile() 
	throws IOException, InterruptedException 
	{
		clienteUDP= new ClienteUDP();
		clienteUDP.createConnection();
		fileEvent.setPath(PathStatus.toClient);
		if(fileEvent.getUserTarget()!=null) 
		{
			for (int i = 0; i < threads.size(); i++)
			{
				if(threads.get(i).getHandlerTCP().getAlias().equalsIgnoreCase(fileEvent.getUserTarget()))
				{
					clienteUDP.setIpAddres(threads.get(i).getHandlerTCP().getIp());
					clienteUDP.sendFile(fileEvent.getDestinationDirectory()+"/"+fileEvent.getFilename(), fileEvent.getDestinationDirectory() );
				}
			}
		}
		if(fileEvent.getUserTarget()==null) 
		{
			for (int i = 0; i < threads.size(); i++)
			{
				
					clienteUDP.setIpAddres(threads.get(i).getHandlerTCP().getIp());
					clienteUDP.sendFile(fileEvent.getDestinationDirectory()+"/"+fileEvent.getFilename(), fileEvent.getDestinationDirectory() );
			}
			
		}
	}

	public void createAndWriteFile() 
	{
		String outputFile = fileEvent.getDestinationDirectory()+"/"+ fileEvent.getFilename();
		if (!new File(fileEvent.getDestinationDirectory()).exists()) {
			new File(fileEvent.getDestinationDirectory()).mkdirs();
		}
		
		
		File dstFile = new File(outputFile);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(dstFile);
			fileOutputStream.write(fileEvent.getFileData());
			fileOutputStream.flush();
			fileOutputStream.close();
			//System.out.println("Output file : " + outputFile + " is successfully saved ");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public void setSocket(DatagramSocket socket) {
		this.socket = socket;
	}

	public ArrayList<ServerThread> getThreads() {
		return threads;
	}

	public void setThreads(ArrayList<ServerThread> threads) {
		this.threads = threads;
	}


	public FileEvent getFileEvent() {
		return fileEvent;
	}


	public void setFileEvent(FileEvent fileEvent) {
		this.fileEvent = fileEvent;
	}
	
	
	
	
}