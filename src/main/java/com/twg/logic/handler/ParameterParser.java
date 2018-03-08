package com.twg.logic.handler;

import com.google.inject.Inject;
import com.twg.logic.db.IDB;

public class ParameterParser implements IParameterParser {

    IDB db;

    @Inject
    public ParameterParser(IDB _db) {
        this.db = _db;
    }

    @Override
    public String parse(String name, String age) {
        return String.format("name: %s, age: %s, userData: %s", name, age, db.getUserData(name));
    }

    @Override
    public void close() throws Exception {
        db.close();
    }
}
