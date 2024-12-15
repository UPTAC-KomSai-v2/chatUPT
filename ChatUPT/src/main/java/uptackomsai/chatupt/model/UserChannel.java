/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.model;

/**
 *
 * @author Lei
 */
public class UserChannel {
    private final int userID;
    private final int chatID;
    private final String role;
    public UserChannel(int userID, int chatID, String role){
        this.userID = userID;
        this.chatID = chatID;
        this.role = role;
    }
    public int getUserID(){
        return userID;
    }
    public int getChatID(){
        return chatID;
    }
    public String getRole(){
        return role;
    }
}
