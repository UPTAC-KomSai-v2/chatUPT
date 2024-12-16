/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.model;

/**
 *
 * @author Lei
 */
public class Message {
    private final int chat_id;
    private final String profile_path;
    private final int user_id;
    private final String username;
    private final String time_sent;
    private final String content;
    private String file_path;
    private boolean is_read;
    public Message(int chat_id, String profile_path, int user_id, String username, String time_sent, 
            String content, String file_path, boolean is_read){
        this.profile_path = profile_path;
        this.chat_id = chat_id;
        this.user_id = user_id;
        this.username = username;
        this.time_sent = time_sent;
        this.content = content;
        this.file_path = file_path;
        this.is_read = is_read;
    }
    public String getProfilePath() {
        return profile_path;
    }
    public int chat_id() {
        return chat_id;
    }
    public int getUserID() {
        return user_id;
    }
    public String getUsername() {
        return username;
    }
    public String getTimeSent() {
        return time_sent;
    }
    public String getContent() {
        return content;
    }
    public String getFilePath() {
        return file_path;
    }
    public boolean getIsRead() {
        return is_read;
    }
}
