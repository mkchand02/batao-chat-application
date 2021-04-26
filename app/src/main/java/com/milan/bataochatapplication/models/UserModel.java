package com.milan.bataochatapplication.models;

public class UserModel {

    public String firstname, lastname, username, email, gender, type, state;//type is user
    public String photo, phone, status, desc;
    public int friendsCount;

    void UserModel(){}

    public UserModel(String firstname, String lastname, String gender, String username, String email,
                     String type, String phone, String state, String photo, String status, String desc,
                     int friendsCount) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.phone = phone;
        this.state = state;
        this.type = type;
        this.friendsCount = friendsCount;
        if(photo != null)
            this.photo = photo;
        else
            this.photo = "https://firebasestorage.googleapis.com/v0/b/wedding-planner-shubhmangalam.appspot.com/o/profile_image_placeholder.png?alt=media&token=e925b42a-606d-40c3-b538-ae7abf18ed1a";
        this.status = status;
        this.desc = desc;
    }

    public UserModel(String firstname, String lastname, String username, String gender, String email, String type,
                      String phone, String state) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.phone = phone;
        this.state = state;
        this.type = type;
        this.photo = "https://firebasestorage.googleapis.com/v0/b/wedding-planner-shubhmangalam.appspot.com/o/profile_image_placeholder.png?alt=media&token=e925b42a-606d-40c3-b538-ae7abf18ed1a";
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }
}

