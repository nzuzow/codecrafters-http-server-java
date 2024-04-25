package models;

import java.util.HashMap;
import java.util.Map;

public class HttpResp {
    private String version;
    private int statusCode;
    private String statusText;
    private String body;
    private Map<String, String> headers;

    public HttpResp(int statusCode) {
        this.version = "HTTP/1.1";
        this.statusCode = statusCode;
        this.statusText = "";
        this.body = "";
        this.headers = new HashMap<String, String>();
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return this.version;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusText() {
        return this.statusText;
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

    public String asString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %s %s\r\n", version, statusCode, statusText));
        if (this.headers != null && this.headers.size() > 0) {
            for (Map.Entry<String, String> header: headers.entrySet()) {
                sb.append(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
            }
            sb.append("\r\n");
        }
        if (body != null && !body.isEmpty()) {
            sb.append(body);
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
