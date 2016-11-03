// IBookManager.aidl
package com.example.administrator.ipc_test;

import com.example.administrator.ipc_test.Book;
import com.example.administrator.ipc_test.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
