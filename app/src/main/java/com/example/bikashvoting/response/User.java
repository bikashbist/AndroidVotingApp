package com.example.bikashvoting.response;

public class User {
    private String _id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String image;
    private String type;
    private String[] votes;
    private boolean verify;


    public User(String firstName,String lastName,String type){
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
    }


    public User(String firstName, String lastName, String username, String password, String type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.type = type;
    }


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String image) {
        this.image = image;
    }

    public User(String firstName, String lastName, String username, String password, String image, String type, String[] votes, boolean verify) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.image = image;
        this.type = type;
        this.votes = votes;
        this.verify = verify;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getVotes() {
        return votes;
    }

    public void setVotes(String[] votes) {
        this.votes = votes;
    }

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }
}
