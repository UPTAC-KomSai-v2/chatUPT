/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.model;

/**
 *
 * @author Lei
 */
public class DirectChat {
    private int user1ID;
    private int user2ID;

    public DirectChat(int user1ID, int user2ID) {
        this.user1ID = user1ID;
        this.user2ID = user2ID;
    }

    public int getUser1ID() {
        return user1ID;
    }

    public int getUser2ID() {
        return user2ID;
    }
}
