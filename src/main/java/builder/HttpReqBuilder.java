package builder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import models.HttpReq;

public class HttpReqBuilder {
    private String method;
    private String path;
    private String version;
    private String body;
    private Map<String, String> headers;

    public HttpReqBuilder() {
        this.headers = new HashMap<String, String>();
    }

    public HttpReqBuilder method(String method) {
        this.method = method;
        return this;
    }

    public HttpReqBuilder path(String path) {
        this.path = path;
        return this;
    }

    public HttpReqBuilder version(String version) {
        this.version = version;
        return this;
    }

    public HttpReqBuilder body(String body) {
        this.body = body;
        return this;
    }

    public HttpReqBuilder header(String key, String val) {
        this.headers.put(key, val);
        return this;
    }

    public HttpReq build() {
        HttpReq req = new HttpReq(method, path, version);
        req.setHeaders(headers);
        if (body != null && !body.isEmpty()) {
            req.setBody(body);
        }
        return req;
    }

    public static HttpReq buildFromInputStream(InputStream is) {
        HttpReqBuilder builder = new HttpReqBuilder();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            // read the first line of the request and create a string array of the
            // space delimited parts within the start line.
            String[] reqParts = reader.readLine().split(" ");

            if (reqParts.length < 3) {
                throw new Exception("Incomplete request line: Method, Path, or Version is missing.");
            }

            // add the method, path, and version to this request
            builder.method(reqParts[0]);
            builder.path(reqParts[1]);
            builder.version(reqParts[2]);

            // add each header to this request
            String hLine;
            while ((hLine = reader.readLine()) != null && !hLine.isEmpty()) {
                int colonIdx = hLine.indexOf(':');
                if (colonIdx > 0) {
                    builder.header(
                        hLine.substring(0, colonIdx).trim(),
                        hLine.substring(colonIdx + 1).trim()
                    );
                }
            }

            // if the content length header is defined, then try to read the body and store it in the HttpReq object.
            int contentLen = Integer.parseInt(builder.headers.getOrDefault("Content-Length", "0"), 10);
            if (contentLen > 0) {
                char[] buffer = new char[contentLen];
                int readCount = reader.read(buffer, 0, contentLen);
                if (readCount == contentLen) {
                    builder.body(String.valueOf(buffer));
                }
            }
            
            //reader.close();
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return builder.build();
    }
}
