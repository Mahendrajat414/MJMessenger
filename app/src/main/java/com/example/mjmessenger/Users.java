package com.example.mjmessenger;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {
    String Profilepic, mail, userName, password, userId, lastMessage, status;

    public Users() {
    }

    public Users(String userId, String userName, String mail, String password, String Profilepic, String status) {
        this.userId = userId;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.Profilepic = Profilepic;
        this.status = status;
    }

    public String getProfilepic() {
        return Profilepic;
    }

    public void setProfilepic(String profilepic) {
        Profilepic = profilepic;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Parcelable implementation
    protected Users(Parcel in) {
        Profilepic = in.readString();
        mail = in.readString();
        userName = in.readString();
        password = in.readString();
        userId = in.readString();
        lastMessage = in.readString();
        status = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Profilepic);
        dest.writeString(mail);
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(userId);
        dest.writeString(lastMessage);
        dest.writeString(status);
    }
}
