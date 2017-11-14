package in.co.sdrc.mychat;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by reale on 18/11/2016.
 */

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private long messageTime;
    private String messageReceiver;
    private boolean isImage;
    private String imageFile;

    public ChatMessage(String messageText, String messageUser, String messageReceiver, boolean isImage, String imageFile) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageReceiver = messageReceiver;
        this.isImage = isImage;
        this.imageFile = imageFile;

        messageTime = new Date().getTime();
    }

    public ChatMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageReceiver() {
        return messageReceiver;
    }

    public void setMessageReceiver(String messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
}
