package com.saikauskas.julius.mint.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmReminderDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "alarmreminder.db";

    public static final int DATABASE_VERSIONS = 1;

    public AlarmReminderDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSIONS);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_ALARM_TABLE = "CREATE TABLE " + AlarmReminderContract.AlarmReminderEntry.TABLE_NAME + " ("
                + AlarmReminderContract.AlarmReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AlarmReminderContract.AlarmReminderEntry.KEY_TITLE + " TEXT, "
                + AlarmReminderContract.AlarmReminderEntry.KEY_DATE + " TEXT, "
                + AlarmReminderContract.AlarmReminderEntry.KEY_TIME + " TEXT, "
                //+ AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT + " TEXT, "
                //+ AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO + " TEXT, "
                //+ AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE + " TEXT, "
                + AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE + " TEXT " + " );";

        //Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_ALARM_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
