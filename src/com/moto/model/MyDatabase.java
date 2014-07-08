package com.moto.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase extends SQLiteOpenHelper{
    
	
	protected String DATABASE_NAME = "moto.db";
	protected int DATABASE_VERSION = 1;     //数据库版本号，不影响表的创建
	protected String TABLE_NAME;
	public MyDatabase(Context context, String name, CursorFactory factory,
                      int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
    
}
