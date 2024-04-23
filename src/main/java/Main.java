import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    
    try {
      serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
      clientSocket = serverSocket.accept(); // Wait for connection from client.
      InputStream is = clientSocket.getInputStream();
      System.out.println("accepted new connection");
      OutputStream os = clientSocket.getOutputStream();
      PrintWriter writer = new PrintWriter(os, true);
      writer.println("HTTP/1.1 200 OK\r\n\r\n");
      clientSocket.close();
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
