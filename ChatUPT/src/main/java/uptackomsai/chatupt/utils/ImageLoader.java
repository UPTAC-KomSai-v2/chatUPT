/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.utils;

/**
 *
 * @author Lei
 */
import javax.swing.ImageIcon;

public class ImageLoader {
    public static ImageIcon loadImageIcon(String imageName) {
        return new ImageIcon(ImageLoader.class.getResource("/images/" + imageName));
    }
}