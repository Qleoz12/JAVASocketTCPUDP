package cliente;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import server.FileEvent;


public class ClienteUDP {
	
	
	private DatagramSocket socket = null;
	private FileEvent event = null;
	 
	 
	public ClienteUDP() {
		// TODO Auto-generated constructor stub
	}

	public void createConnection() {
		try {

			socket = new DatagramSocket();
	
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void sendFile(String sourceFilePath,String destinationPath) 
	throws IOException, InterruptedException
	{
		InetAddress IPAddress = InetAddress.getByName("localHost");
		byte[] incomingData = new byte[1024];
		event = getFileEvent(sourceFilePath,destinationPath);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(outputStream);
		os.writeObject(event);
		byte[] data = outputStream.toByteArray();
		DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 9876);
		socket.send(sendPacket);
		System.out.println("File sent from client");
		DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
		socket.receive(incomingPacket);
		String response = new String(incomingPacket.getData());
		Thread.sleep(2000);
		System.out.println("Response from server:" + response);
	}
	
	public FileEvent getFileEvent(String sourceFilePath,String destinationPath)
	{
		FileEvent fileEvent = new FileEvent();
		String fileName = sourceFilePath.substring(sourceFilePath.lastIndexOf("/") + 1, sourceFilePath.length());
		String path = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("/") + 1);
		fileEvent.setDestinationDirectory(destinationPath);
		fileEvent.setFilename(fileName);
		fileEvent.setSourceDirectory(sourceFilePath);
		File file = new File(sourceFilePath);
		if (file.isFile()) {
			try {
				DataInputStream diStream = new DataInputStream(new FileInputStream(file));
				long len = (int) file.length();
				byte[] fileBytes = new byte[(int) len];
				int read = 0;
				int numRead = 0;
				while (read < fileBytes.length
						&& (numRead = diStream.read(fileBytes, read, fileBytes.length - read)) >= 0) {
					read = read + numRead;
				}
				fileEvent.setFileSize(len);
				fileEvent.setFileData(fileBytes);
				fileEvent.setStatus("Success");
			} catch (Exception e) {
				e.printStackTrace();
				fileEvent.setStatus("Error");
			}
		} else {
			System.out.println("path specified is not pointing to a file");
			fileEvent.setStatus("Error");
		}
		return fileEvent;
	}
}
