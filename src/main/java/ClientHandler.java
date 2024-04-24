import java.net.Socket;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

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
            BufferedReader in = new BufferedReader(new InputStreamReader(is));

            // read the first line of the request and create a string array of the
            // space delimited parts within the start line.
            String[] reqParts = in.readLine().split(" ");

            // create an HttpReq object to represent the request
            HttpReq req = new HttpReq(reqParts[0], reqParts[1], reqParts[2]);

            String hLine;
            while ((hLine = in.readLine()) != null && !hLine.isEmpty()) {
                int colonIdx = hLine.indexOf(':');
                if (colonIdx > 0) {
                    req.addHeader(
                        hLine.substring(0, colonIdx).trim(),
                        hLine.substring(colonIdx + 1).trim()
                    );
                }
            }
            
            String path = (req.getPath() != null) ? req.getPath() : "";

            // determine proper response based on path
            HttpResp resp;
            if (path.startsWith("/files/")) {
                try {
                    String fileName = path.substring(7);
                    String dir = this.cfg.get("--directory");
                    BufferedReader file = new BufferedReader(new FileReader(dir + fileName));
                    StringBuilder fileSB = new StringBuilder();
                    String line;
                    while (file.ready() && (line = file.readLine()) != null) {
                        fileSB.append(line);
                    }
                    file.close();
                    String body = fileSB.toString();

                    resp = new HttpRespBuilder()
                            .version("HTTP/1.1")
                            .statusCode(200)
                            .statusText("OK")
                            .addHeader("Content-Type", "application/octet-stream")
                            .addHeader("Content-Length", String.valueOf(body.length()))
                            .body(body)
                            .build();
                } catch(Exception e) {
                    resp = buildErrorResp();
                }
            } else if (path.startsWith("/echo")) {
                String body = (path.startsWith("/echo/")) ? path.substring(6) : path.substring(5);
                resp = new HttpRespBuilder()
                        .version("HTTP/1.1")
                        .statusCode(200)
                        .statusText("OK")
                        .addHeader("Content-Type", "text/plain")
                        .addHeader("Content-Length", String.valueOf(body.length()))
                        .body(body)
                        .build();
            } else if (path.startsWith("/user-agent")) {
                String body = req.getHeader("User-Agent");
                resp = new HttpRespBuilder()
                        .version("HTTP/1.1")
                        .statusCode(200)
                        .statusText("OK")
                        .addHeader("Content-Type", "text/plain")
                        .addHeader("Content-Length", String.valueOf(body.length()))
                        .body(body)
                        .build();
            } else if ("/".equals(path)) {
                resp = new HttpRespBuilder()
                        .version("HTTP/1.1")
                        .statusCode(200)
                        .statusText("OK")
                        .addHeader("Content-Type", "text/plain")
                        .addHeader("Content-Length", "0")
                        .build();
            } else {
                resp = buildErrorResp();
            }

            // create an output stream and print writer for the response
            OutputStream os = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(os, true);
            writer.println(resp.asString());
            writer.close();
            in.close();
            os.close();
            this.clientSocket.close();
        } catch (Exception e) {
            System.out.println("ClientHandler Exception: " + e.getMessage());
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
