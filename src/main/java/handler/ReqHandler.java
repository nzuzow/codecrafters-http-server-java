package handler;

import java.util.Map;
import models.HttpReq;
import models.HttpResp;

public interface ReqHandler {
    HttpResp handle(HttpReq req, Map<String, String> cfg) throws Exception;    
}
