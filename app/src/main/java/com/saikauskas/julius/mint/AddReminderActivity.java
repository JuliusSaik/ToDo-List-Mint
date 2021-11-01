package com.saikauskas.julius.mint;


import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.saikauskas.julius.mint.data.AlarmReminderContract;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;


public class AddReminderActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, LoaderManager.LoaderCallbacks<Cursor> {


    private static final int EXISTING_VEHICLE_LOADER = 0;

    public String savedTitle;


    public EditText etAddTask;
    private Toolbar mToolbar;
    private TextView tvPickDate, tvPickTime;//tvRepeat tvPickRepeatNo, tvPickRepeatType;
    private int mYear, mMonth, mHour, mMinute, mDay;
    //private long mRepeatTime;
    //private Switch repeatSwitch;
    public static String mTitle;
    private String mTime;
    private String mDate;
    //private String mRepeat;
    //private String mRepeatNo;
    //private String mRepeatType;
    private String mActive;

    //private FloatingActionButton mFAB1;
    //private FloatingActionButton mFAB2;


    private Uri mCurrentReminderUri;
    private boolean mVehicleHasChanged = false;

    //Values for orientation change
    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    //private static final String KEY_REPEAT = "repeat_key";
    //private static final String KEY_REPEAT_NO = "repeat_no_key";
    //private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_ACTIVE = "active_key";


    //Constant values in milliseconds
    public static final long milMinute = 60000L;
    public static final long milHour = 3600000L;
    public static final long milDay = 86400000L;
    public static final long milWeek = 604800000L;
    public static final long milMonth = 2592000000L;

    public static String title;

    SharedPref sharedPref;

    Calendar now = Calendar.getInstance();
    Calendar cal_alarm = Calendar.getInstance();

    public static int rowsDeleted;

    public static int notiResultCode;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mVehicleHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);

        if (sharedPref.loadLightModeState() == true) {
            setTheme(R.style.lightThemeApp);

        } else {
            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);



        ImageView ivBackArrowMain = findViewById(R.id.ivBackArrowMain);

        ImageView dateIconDark = findViewById(R.id.ivDate);
        ImageView dateIconLight = findViewById(R.id.ivDateLight);

        ImageView timeIconDark = findViewById(R.id.ivTime);
        ImageView timeIconLight = findViewById(R.id.ivTimeLight);

        ImageView mintIconDark = findViewById(R.id.ReminderIconDark);
        ImageView mintIconLight = findViewById(R.id.ReminderIconLight);

        TextView reminderActivityTxt = findViewById(R.id.addremindertext);


        //If it's light mode change Icon
        if (sharedPref.loadLightModeState() == true) {
            mintIconDark.setVisibility(View.INVISIBLE);
            mintIconLight.setVisibility(View.VISIBLE);

            dateIconDark.setVisibility(View.INVISIBLE);
            dateIconLight.setVisibility(View.VISIBLE);

            timeIconDark.setVisibility(View.INVISIBLE);
            timeIconLight.setVisibility(View.VISIBLE);
        } else {
            mintIconDark.setVisibility(View.VISIBLE);
            mintIconLight.setVisibility(View.INVISIBLE);

            dateIconDark.setVisibility(View.VISIBLE);
            dateIconLight.setVisibility(View.INVISIBLE);

            timeIconDark.setVisibility(View.VISIBLE);
            timeIconLight.setVisibility(View.INVISIBLE);
        }

        ivBackArrowMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddReminderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slideright, R.anim.slideleft);
            }
        });


        Intent intent = getIntent();
        mCurrentReminderUri = intent.getData();


        if (mCurrentReminderUri == null) {
            reminderActivityTxt.setText("Add Task");

            invalidateOptionsMenu();
        } else {
            reminderActivityTxt.setText("Edit Task");

            getLoaderManager().initLoader(EXISTING_VEHICLE_LOADER, null, this);
        }

        mToolbar = findViewById(R.id.toolbar);

        etAddTask = findViewById(R.id.etAddTask);
        tvPickDate = findViewById(R.id.tvPickDate);
        tvPickTime = findViewById(R.id.tvPickTime);
        //tvRepeat = findViewById(R.id.tvRepeat);
        //tvPickRepeatNo = findViewById(R.id.tvPickRepeatNo);
        //tvPickRepeatType = findViewById(R.id.tvPickRepeatType);
        //repeatSwitch = findViewById(R.id.repeatSwitch);
        //mFAB1 = findViewById(R.id.fabAlarmOff);
        //mFAB2 = findViewById(R.id.fabAlarmOn);

        mActive = "true";
        //mRepeat = "true";
        //mRepeatNo = Integer.toString(1);
        //mRepeatType = "Hour";

        mHour = now.get(Calendar.HOUR_OF_DAY);
        mMinute = now.get(Calendar.MINUTE);
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH) + 1;
        mDay = now.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;


        etAddTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                etAddTask.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tvPickDate.setText(mDate);
        tvPickTime.setText(mTime);
        //tvPickRepeatNo.setText(mRepeatNo);
        //tvPickRepeatType.setText(mRepeatType);
        //tvRepeat.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");

        if (savedInstanceState != null) {

            savedTitle = savedInstanceState.getString(KEY_TITLE);
            etAddTask.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            tvPickTime.setText(savedTime);
            mTime = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            tvPickDate.setText(savedDate);
            mDate = savedDate;

            /*
            String savedRepeat = savedInstanceState.getString(KEY_REPEAT);
            tvRepeat.setText(savedRepeat);
            mRepeat = savedRepeat;

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            tvPickRepeatNo.setText(savedRepeatNo);
            mRepeatNo = savedRepeatNo;

            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            tvPickRepeatType.setText(savedRepeatType);
            mRepeatType = savedRepeatType;
            */

            mActive = savedInstanceState.getString(KEY_ACTIVE);

        }



        /*
        if (mActive.equals("false")) {
            starUnfilled.setVisibility(View.VISIBLE);
            starFilled.setVisibility(View.GONE);
        } else if (mActive.equals("true")){
            starUnfilled.setVisibility(View.GONE);
            starFilled.setVisibility(View.VISIBLE);
        }
        */



        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);



        AdView adView = findViewById(R.id.adView);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                if (isDestroyed()){
                    adView.destroy();
                    return;
                }
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, etAddTask.getText());
        outState.putCharSequence(KEY_TIME, tvPickTime.getText());
        outState.putCharSequence(KEY_DATE, tvPickDate.getText());
        //outState.putCharSequence(KEY_REPEAT, tvRepeat.getText());
        //outState.putCharSequence(KEY_REPEAT_NO, tvPickRepeatNo.getText());
        //outState.putCharSequence(KEY_REPEAT_TYPE, tvPickRepeatType.getText());
    }


    public void setTime(View view) {

        TimePickerDialog tpd = TimePickerDialog.newInstance(
                AddReminderActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );

        tpd.setThemeDark(true);
        tpd.show(getFragmentManager(), "Timepickerdialog");

    }

    public void setDate(View view) {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AddReminderActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dpd.show(getFragmentManager(), "Datepickerdialog");

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int month, int dayOfMonth) {
        month++;
        mDay = dayOfMonth;
        mMonth = month;
        mYear = year;
        mDate = dayOfMonth + "/" + month + "/" + year;
        tvPickDate.setText(mDate);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {


        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        tvPickTime.setText(mTime);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentReminderUri == null) {
            MenuItem menuItem = menu.findItem(R.id.discard_reminder);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_reminder:

                if (etAddTask.getText().toString().length() == 0) {
                    etAddTask.setError("Add something to do first");
                } else {
                    saveReminder();
                    finish();
                    overridePendingTransition(R.anim.slideright, R.anim.slideleft);
                }
                return true;

            //Respons to a click on the delete menu option
            case R.id.discard_reminder:
                showDeleteConfirmationDialog();
                return true;

        }

        return super.onOptionsItemSelected(item);

    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage("Delete this task?");
        builder.setPositiveButton("Delete", (dialog, id) -> {
            deleteReminder();
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }




    public void deleteReminder() {
        // Only perform the delete if this is an existing reminder
        //AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        if (mCurrentReminderUri != null) {
            rowsDeleted = getContentResolver().delete(mCurrentReminderUri, null, null);
            Intent intent = new Intent(AddReminderActivity.this, MyBroadcastReceiver.class);
            PendingIntent.getBroadcast(AddReminderActivity.this, notiResultCode++, intent, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
            //alarmManager.cancel(pendingIntentGet);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Error deleting the task", Toast.LENGTH_SHORT).show();
            }
        }

        finish();

        overridePendingTransition(R.anim.slideright, R.anim.slideleft);
    }

    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                //AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                //AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                //AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
        };

        return new CursorLoader(this,
                mCurrentReminderUri,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
            int dateColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_DATE);
            int timeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TIME);
            //int repeatColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT);
            //int repeatNoColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
            //int repeatTypeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE);

            mTitle = cursor.getString(titleColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            //String repeat = cursor.getString(repeatColumnIndex);
            //String repeatNo = cursor.getString(repeatNoColumnIndex);
            //String repeatType = cursor.getString(repeatTypeColumnIndex);

            etAddTask.setText(mTitle);
            tvPickDate.setText(date);
            tvPickTime.setText(time);
            //tvPickRepeatNo.setText(repeatNo);
            //tvPickRepeatType.setText(repeatType);
            //tvRepeat.setText("Every" + " " + repeatNo + " " + repeatType + "(s)");

            /*
            if (repeat == null){
                repeatSwitch.setChecked(false);
                tvRepeat.setText("Off");
            } else if (repeat.equals("false")) {
                repeatSwitch.setChecked(false);
                tvRepeat.setText("Off");
            } else if (repeat.equals("true")){
                repeatSwitch.setChecked(true);
            }
            */
        }

    }
    private void saveReminder() {

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Date date = new Date(System.currentTimeMillis());

        now.setTime(date);
        cal_alarm.setTime(date);

        //now.setTimeInMillis(System.currentTimeMillis());
        //cal_alarm.setTimeInMillis(System.currentTimeMillis());

        //cal_alarm.set(Calendar.MONTH, mMonth);
        //cal_alarm.set(Calendar.DAY_OF_MONTH, mDay );


        cal_alarm.set(Calendar.HOUR_OF_DAY, mHour);
        cal_alarm.set(Calendar.MINUTE, mMinute);
        //cal_alarm.set(Calendar.SECOND, 0);



        if (cal_alarm.before(now)) {
            cal_alarm.add(Calendar.DATE, 1);
        }

        ContentValues values = new ContentValues();


        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, mTitle);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, mDate);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, mTime);
        //values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT, mRepeat);
        //values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);
        //values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);

        now.set(Calendar.MONTH, mMonth);
        now.set(Calendar.YEAR, mYear);
        now.set(Calendar.DAY_OF_MONTH, mDay);
        now.set(Calendar.HOUR_OF_DAY, mHour);
        now.set(Calendar.MINUTE, mMinute);
        now.set(Calendar.SECOND, 0);

        long selectedTimestamp = now.getTimeInMillis();

        /*

        if (mRepeatType.equals("Minute")){
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
        } else if (mRepeatType.equals("Hour")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;
        } else if (mRepeatType.equals("Day")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
        } else if (mRepeatType.equals("Week")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
        } else if (mRepeatType.equals("Month")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;


        }
        */

        if (mCurrentReminderUri == null) {
            Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "Error with saving task", Toast.LENGTH_SHORT).show();
            }

        } else {

            int rowsAffected = getContentResolver().update(mCurrentReminderUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, "Error with updating the task", Toast.LENGTH_SHORT).show();
            }

        }


        Intent intent = new Intent(AddReminderActivity.this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddReminderActivity.this, notiResultCode++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);

    }
    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }
}
