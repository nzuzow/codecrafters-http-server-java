public class ResponseItem {
    private int status;
    private String contentType;
    private String content;
  
    public ResponseItem(int status) {
      this.status = status;
      this.contentType = "";
      this.content = "";
    }
  
    private String addHeader() {
      String out = "HTTP/1.1 ";
      switch(this.status) {
        case 404:
          out += "404 Not Found";
          break;
        default:
          out += "200 OK";
          break;
      }
      return out + "\r\n";
    }
  
    private String addContentType() {
      String out = "";
      if (this.contentType.length() > 0) {
        out += "Content-Type: " + this.contentType + "\r\n";
      }
      return out;
    }
  
    private String addContent() {
      String out = "";
      if (this.content.length() > 0) {
        out += "Content-Length: " + this.content.length() + "\r\n\r\n";
        out += this.content + "\r\n";
      }
      return out;
    }
  
    public void setStatus(int status) {
      this.status = status;
    }
  
    public void setContentType(String contentType) {
      this.contentType = contentType;
    }
  
    public void setContent(String content) {
      this.content = content;
    }
  
    public String asString() {
      String out = addHeader();
      out += addContentType();
      out += addContent();
      out += "\r\n";
      return out;
    }
  }