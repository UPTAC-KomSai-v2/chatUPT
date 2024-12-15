/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.model;

/**
 *
 * @author Lei
 */
public class ChatMessage {
    private int chatID;
    private boolean isChannel;

    public ChatMessage(int chatID, boolean isChannel) {
        this.chatID = chatID;
        this.isChannel = isChannel;
    }
    
    public int getChatID(){
        return chatID;
    }
    
    public boolean getIsChannel(){
        return isChannel;
    }

}
