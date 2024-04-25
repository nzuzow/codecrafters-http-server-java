package builder;

import java.util.HashMap;
import java.util.Map;

import models.HttpResp;

public class HttpRespBuilder {
    private String version;
    private int statusCode;
    private String statusText;
    private String body;
    private Map<String, String> headers;

    public HttpRespBuilder() {
        this.headers = new HashMap<String, String>();
    }

    public HttpRespBuilder version(String version) {
        this.version = version;
        return this;
    }

    public HttpRespBuilder statusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public HttpRespBuilder statusText(String statusText) {
        this.statusText = statusText;
        return this;
    }

    public HttpRespBuilder body(String body) {
        this.body = body;
        return this;
    }

    public HttpRespBuilder addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public HttpResp build() {
        HttpResp resp = new HttpResp(this.statusCode);
        resp.setStatusText(this.statusText);
        resp.setVersion(this.version);
        resp.setBody(this.body);
        resp.setHeaders(this.headers);
        return resp;
    }
}
