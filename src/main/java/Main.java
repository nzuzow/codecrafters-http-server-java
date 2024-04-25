import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.HashMap;

import handler.ClientHandler;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // Get a config map from the arguments passed in from the command line
    Map<String, String> cfg = buildConfig(args);

    // Uncomment this block to pass the first stage
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    
    try {
      serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
      
      while (true) {
        clientSocket = serverSocket.accept(); // Wait for connection from client.
        System.out.println("accepted new connection");

        Thread clientThread = new Thread(new ClientHandler(clientSocket, cfg));
        clientThread.start();
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }

  private static Map<String, String> buildConfig(String[] args) {
    // Loop through the arguments passed in and add them to a config map.
    // Currently assuming arguments will be defined on the command line as <key> <value>, so
    // increment through the argument list by 2.
    Map<String, String> output = new HashMap<String, String>();
    int i = 0;
    while (i < args.length - 1) {
      output.put(args[i], args[i + 1]);
      i += 2;
    }
    return output;
  }
}
