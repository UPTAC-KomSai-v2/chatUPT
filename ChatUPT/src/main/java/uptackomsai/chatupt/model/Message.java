package uptackomsai.chatupt.model;

public class Message {
    private String type; // "register" or "connect"
    private String content;

    public Message(String type, String content) {
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
