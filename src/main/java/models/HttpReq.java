package models;

import java.util.HashMap;
import java.util.Map;

public class HttpReq {
    private String method;
    private String path;
    private String version;
    private String body;
    private Map<String, String> headers;

    public HttpReq(String method, String path) {
        this(method, path, "HTTP/1.1");
    }

    public HttpReq(String method, String path, String version) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.body = "";
        this.headers = new HashMap<String, String>();        
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return this.method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return this.version;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getHeader(String key) {
        String out = "";
        if (this.headers.containsKey(key)) {
            out = this.headers.get(key);
        }
        return out;
    }
}
