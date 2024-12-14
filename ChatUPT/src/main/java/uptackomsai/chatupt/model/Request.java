package uptackomsai.chatupt.model;

public class Request {
    private String type; // "register" or "connect"
    private String content;

    public Request(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
