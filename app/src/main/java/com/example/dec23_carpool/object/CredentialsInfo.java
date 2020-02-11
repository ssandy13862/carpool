package com.example.dec23_carpool.object;

import java.io.Serializable;

public class CredentialsInfo implements Serializable {
    private String email;
    private String password;

    public CredentialsInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return "email:"+email+" password:"+password;
    }
}
