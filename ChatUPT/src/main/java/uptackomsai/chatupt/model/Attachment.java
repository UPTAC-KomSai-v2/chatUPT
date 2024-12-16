/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.model;

/**
 *
 * @author ASUS
 */
public class Attachment {
        private String file_name;
    private String file_path;
    private String file_type;
    private int file_size;

    // Constructor
    public Attachment(String file_name, String file_path, String file_type, int file_size) {
        this.file_name = file_name;
        this.file_path = file_path;
        this.file_type = file_type;
        this.file_size = file_size;
    }
    
    // Getters and setters
    public String getFileName() {
        return file_name;
    }

    public void setFileName(String file_name) {
        this.file_name = file_name;
    }
    
    public String getFilePath() {
        return file_path;
    }
    
    public String getFileType() {
        return file_type;
    }
    
    public int getFileSize() {
        return file_size;
    }
}
