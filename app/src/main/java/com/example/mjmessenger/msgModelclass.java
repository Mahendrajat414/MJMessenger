package com.example.mjmessenger;

public class msgModelclass {
    String message;
    String senderid;
    long timeStamp;
    String imageUrl; // Add this line

    public msgModelclass() {
    }

    public msgModelclass(String message, String senderid, long timeStamp) {
        this.message = message;
        this.senderid = senderid;
        this.timeStamp = timeStamp;
    }

    public msgModelclass(String senderid, long timeStamp, String imageUrl) { // Add this constructor
        this.senderid = senderid;
        this.timeStamp = timeStamp;
        this.imageUrl = imageUrl;
    }

    // ... (getters and setters for imageUrl)

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
