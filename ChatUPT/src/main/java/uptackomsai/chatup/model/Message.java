/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatup.model;

/**
 *
 * @author Admin
 */
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
