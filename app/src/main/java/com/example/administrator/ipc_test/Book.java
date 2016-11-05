package com.example.administrator.ipc_test;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/29.
 */
public class Book implements Parcelable {
    private int mBookId;
    private String mBookName;

    @Override
    public String toString() {
        return "Book{" +
                "mBookId=" + mBookId +
                ", mBookName='" + mBookName + '\'' +
                '}';
    }

    public Book(int id ,String name){
        mBookId = id;
        mBookName = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mBookId);
        dest.writeString(this.mBookName);
    }

    public Book() {
    }

    protected Book(Parcel in) {
        this.mBookId = in.readInt();
        this.mBookName = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int getmBookId() {
        return mBookId;
    }

    public void setmBookId(int mBookId) {
        this.mBookId = mBookId;
    }

    public String getmBookName() {
        return mBookName;
    }

    public void setmBookName(String mBookName) {
        this.mBookName = mBookName;
    }


}
