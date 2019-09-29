package com.geerkbrains.netty.example.common;

public class ClientConnection extends AbstractMessage {
    String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    String pass;

    public ClientConnection(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

}
