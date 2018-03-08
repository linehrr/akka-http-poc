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
    public void close() throws Exception {

    }
}
