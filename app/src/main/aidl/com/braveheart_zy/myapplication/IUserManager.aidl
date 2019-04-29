// IUserManager.aidl
package com.braveheart_zy.myapplication;
import com.braveheart_zy.myapplication.User;
// Declare any non-default types here with import statements

interface IUserManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<User> getUserList();
    void addUser(in User user);
}
