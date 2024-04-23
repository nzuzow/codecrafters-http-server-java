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
      ResponseItem resp;
      if (path != null && path.startsWith("/echo")) {
        String extraPath = (path.startsWith("/echo/")) ? path.substring(6) : path.substring(5);
        resp = new ResponseItem(200);
        resp.setContentType("text/plain");
        resp.setContent(extraPath);
      } else if (path != null && path.startsWith("/user-agent")) {
        resp = new ResponseItem(200);
        resp.setContentType("text/plain");
        String line = brIn.readLine();
        while (line != null) {
          String[] parts = line.split(" ");
          if ("User-Agent:".equals(parts[0])) {
            resp.setContent(parts[1]);
            line = null;
          } else {
            line = brIn.readLine();
          }
        }
      } else if ("/".equals(path)) {
        resp = new ResponseItem(200);
      } else {
        resp = new ResponseItem(404);
      }
      writer.println(resp.asString());
      writer.close();
      brIn.close();
      clientSocket.close();
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
