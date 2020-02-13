package com.example.bikashvoting.response;

public class LoginResponse {
    private int code;
    private String status;
    private String token;

    public LoginResponse(String status, String token) {
        this.status = status;
        this.token = token;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
