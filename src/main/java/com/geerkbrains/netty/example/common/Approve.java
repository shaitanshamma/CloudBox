package com.geerkbrains.netty.example.common;

public class Approve {

    public boolean isAuthorizated() {
        return isAuthorizated;
    }

    boolean isAuthorizated;

    public Approve(boolean isAuthorizated) {
        this.isAuthorizated = isAuthorizated;
    }

}
