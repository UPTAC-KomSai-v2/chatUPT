/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.model;

/**
 *
 * @author Lei
 */
public class UserRole {
    private final int userID;
    private final int chatID;
    public UserRole(int chatID, int userID){
        this.userID = userID;
        this.chatID = chatID;
    }
    public int getUserID(){
        return userID;
    }
    public int getChatID(){
        return chatID;
    }
}
