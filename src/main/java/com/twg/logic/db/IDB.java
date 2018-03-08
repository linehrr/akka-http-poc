package com.twg.logic.db;

public interface IDB extends AutoCloseable {
    String getUserData(String name);
}
