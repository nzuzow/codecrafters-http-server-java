package handler;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import builder.HttpReqBuilder;
import builder.HttpRespBuilder;
import handler.impl.GetReqHandler;
import handler.impl.PostReqHandler;
import models.HttpReq;
import models.HttpResp;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Map<String, String> cfg;

    public ClientHandler(Socket clientSocket, Map<String, String> cfg) {
        this.clientSocket = clientSocket;
        this.cfg = cfg;
    }

    @Override
    public void run() {
        try {
            // read the input
            InputStream is = this.clientSocket.getInputStream();

            // build an HttpReq object from the input stream to represent the request
            HttpReq req = HttpReqBuilder.buildFromInputStream(is);

            // utilize the proper handler to process the request.
            HttpResp resp = null;
            try {
                switch(req.getMethod()) {
                    case "POST":
                        resp = new PostReqHandler().handle(req, cfg);
                        break;
                    case "GET":
                        resp = new GetReqHandler().handle(req, cfg);
                        break;
                }

                if (resp == null) {
                    resp = buildErrorResp();
                }
            } catch(Exception e) {
                System.out.println("Exception occurred processing request: " + e.getMessage());
                resp = buildErrorResp();
            }

            // create an output stream and print writer for the response
            OutputStream os = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(os, true);
            writer.println(resp.asString());
            writer.close();
            os.close();
        } catch (Exception e) {
            System.out.println("ClientHandler Exception: " + e.getMessage());
        } finally {
            try {
                this.clientSocket.close();
            } catch(Exception e) {
                System.out.println("Exception occurred closing the client socket: " + e.getMessage());
            }
        }
    }

    private HttpResp buildErrorResp() {
        return new HttpRespBuilder()
                .version("HTTP/1.1")
                .statusCode(404)
                .statusText("Not Found")
                .addHeader("Content-Type", "text/plain")
                .addHeader("Content-Length", "0")
                .build();
    }
}
