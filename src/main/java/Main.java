import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
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
      System.out.println("accepted new connection");
      
      // read the input
      InputStream is = clientSocket.getInputStream();
      BufferedReader brIn = new BufferedReader(new InputStreamReader(is));

      // read the first line of the request and create a string array of the parts within
      // the start line, delimited by spaces.
      String[] sLineParts = brIn.readLine().split(" ");
      String path = sLineParts[1];
      
      // create an output stream and print writer for the response
      OutputStream os = clientSocket.getOutputStream();
      PrintWriter writer = new PrintWriter(os, true);

      // determine proper response based on path
      String outLine = "";
      if (path != null && path.startsWith("/echo")) {
        String extraPath = (path.startsWith("/echo/")) ? path.substring(6) : path.substring(5);
        outLine = "HTTP/1.1 200 OK\r\n";
        outLine += "Content-Type: text/plain\r\n";
        outLine += "Content-Length: " + extraPath.length() + "\r\n\r\n";
        outLine += extraPath + "\r\n";
      } else if ("/".equals(path)) {
        outLine = "HTTP/1.1 200 OK\r\n\r\n";
      } else {
        outLine = "HTTP/1.1 404 Not Found\r\n\r\n";
      }
      writer.println(outLine);
      writer.close();
      brIn.close();
      clientSocket.close();
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
