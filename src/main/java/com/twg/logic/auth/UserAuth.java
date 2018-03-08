package com.twg.logic.auth;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.twg.logic.db.IDB;
import org.json.JSONObject;


@Singleton
public class UserAuth implements IAuth {

    IDB db;

    @Inject
    public UserAuth(IDB _db) {
        this.db = _db;
    }

    @Override
    public boolean validateUser(String json) {
        JSONObject jsonObj = new JSONObject(json);

        long userId = jsonObj.getLong("userId");
        String passwd = jsonObj.getString("passwd");
        return db.existUser(userId, passwd);
    }
}
