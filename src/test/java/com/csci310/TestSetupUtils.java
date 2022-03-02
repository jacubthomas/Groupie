package com.csci310;

import com.csci310.db.entities.User;

public class TestSetupUtils {

    private static int counter = 0;

    public User makeUser() {
        return makeUser("testUsername" + Integer.toString(counter++), "testPassword" + Integer.toString(counter++));
    }

    public User makeUser(String username, String password) {
        return new User(username, password);
    }
}
