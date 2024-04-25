package handler.impl;

import java.io.FileWriter;
import java.util.Map;
import builder.HttpRespBuilder;
import handler.ReqHandler;
import models.HttpReq;
import models.HttpResp;

public class PostReqHandler implements ReqHandler {
    
    @Override
    public HttpResp handle(HttpReq req, Map<String, String> cfg) throws Exception {
        String path = req.getPath();
        HttpResp resp = null;
        
        if (path.startsWith("/files/")) {
            String fileName = path.substring(7);
            String dir = cfg.getOrDefault("--directory", "");

            try {
                String body = req.getBody();
                FileWriter fileWriter = new FileWriter(dir + fileName);
                fileWriter.write(body);
                fileWriter.close();
                System.out.println("Successfully wrote to the file.");
                resp = new HttpRespBuilder()
                        .version("HTTP/1.1")
                        .statusCode(201)
                        .statusText("Created")
                        .addHeader("Content-Type", "text/plain")
                        .addHeader("Content-Length", "0")
                        .build();
            } catch(Exception e) {
                throw new Exception("Error Processing POST request. " + e.getMessage());
            }
        }

        return resp;
    }
}
