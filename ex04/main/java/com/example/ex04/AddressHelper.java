package com.example.ex04;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AddressHelper extends SQLiteOpenHelper {
    public AddressHelper(@Nullable Context context) {
        super(context, "address.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table address(_id integer primary key autoincrement, name text, phone text, juso text, photo text)");
        db.execSQL("insert into address values(null, '홍실동', '010-1010-2020', '인천 서구', '')");
        db.execSQL("insert into address values(null, '심청이', '010-2020-3030', '서울 강동구', '')");
        db.execSQL("insert into address values(null, '성춘향', '010-4040-5050', '대구 동구', '')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
