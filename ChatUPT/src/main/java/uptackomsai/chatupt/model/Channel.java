/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.model;

/**
 *
 * @author Lei
 */
public class Channel {
    private String channel_name; 
    private Boolean is_private = false;
    private int creatorID;

    public Channel(String channel_name, Boolean is_private, int creatorID) {
        this.channel_name = channel_name;
        this.is_private = is_private;
        this.creatorID = creatorID;
    }

    public String getChannelName() {
        return channel_name;
    }

    public Boolean getIsPrivate() {
        return is_private;
    }
    
    public int getCreatorID(){
        return creatorID;
    }
}
