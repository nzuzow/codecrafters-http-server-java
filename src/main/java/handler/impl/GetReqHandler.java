package handler.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;

import builder.HttpRespBuilder;
import handler.ReqHandler;
import models.HttpReq;
import models.HttpResp;

public class GetReqHandler implements ReqHandler {

    @Override
    public HttpResp handle(HttpReq req, Map<String, String> cfg) throws Exception {
        String path = req.getPath();
        HttpResp resp = null;

        if (path.startsWith("/files/")) {
            String fileName = path.substring(7);
            String dir = cfg.getOrDefault("--directory", "");

            try {
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
                throw new Exception("Exception occurred processing the GET request: " + e.getMessage());
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
            resp = new HttpRespBuilder()
                    .version("HTTP/1.1")
                    .statusCode(404)
                    .statusText("Not Found")
                    .addHeader("Content-Type", "text/plain")
                    .addHeader("Content-Length", "0")
                    .build();
        }

        return resp;
    }
}
