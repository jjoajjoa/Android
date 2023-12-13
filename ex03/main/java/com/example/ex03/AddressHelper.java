package com.example.ex03;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AddressHelper extends SQLiteOpenHelper {
    public AddressHelper(@Nullable Context context) { //DB생성
        super(context, "address.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //Table생성
        db.execSQL("create table address(_id integer primary key autoincrement, name text, phone text, juso text, photo text)"); //_id:커서어뎁터쓰려면
        db.execSQL("insert into address values(null, 'jjoraeng', '010-1010-1010', 'Youngdeungpo St.', '')");
        db.execSQL("insert into address values(null, 'buchu', '010-2020-2020', 'Uijeongbu St.', '')");
        db.execSQL("insert into address values(null, 'mongchung', '010-3030-3030', 'Deajeon St.', '')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
