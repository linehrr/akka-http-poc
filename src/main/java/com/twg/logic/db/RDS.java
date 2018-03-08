package com.twg.logic.db;

import com.google.inject.Singleton;

@Singleton
public class RDS implements IDB {

    public RDS() {

    }

    @Override
    public String getUserData(String name) {
        synchronized (this) {
            return String.format("hello world - %s", name);
        }
    }

    @Override
    public boolean existUser(long userId, String passwd) {
        if(userId == 123123 && passwd.equals("hello")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void close() throws Exception {

    }
}
