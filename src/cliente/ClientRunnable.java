package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRunnable implements Runnable {

    private Socket socket;
    private BufferedReader input;
    // private PrintWriter output;

    public ClientRunnable(Socket s) throws IOException {
        this.socket = s;
        this.input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
        // this.output = new PrintWriter(socket.getOutputStream(),true);
    }
    @Override
    public void run() {
        
            try {
                while(!socket.isClosed()) {
                    
	                	String response = input.readLine();
	                    System.out.println("recibiendo mensaje");
	                    System.out.println(response);
	                    String message = ( "(me)" + " : " );
	                    System.out.print(message);
                                        
                }
            } catch (IOException e)
            {
            	System.out.print(" deslogueo ");
            	
            } finally {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }
    
}
