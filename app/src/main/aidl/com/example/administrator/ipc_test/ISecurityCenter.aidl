// ISecurityCenter.aidl
package com.example.administrator.ipc_test;

// Declare any non-default types here with import statements

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}
