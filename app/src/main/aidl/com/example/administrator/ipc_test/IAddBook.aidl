// IAddBook.aidl
package com.example.administrator.ipc_test;

// Declare any non-default types here with import statements
import com.example.administrator.ipc_test.Book;
interface IAddBook {
    void add(in Book book);
}
