package com.saikauskas.julius.mint;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;


import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.saikauskas.julius.mint.data.AlarmReminderContract;
import com.saikauskas.julius.mint.data.AlarmReminderDbHelper;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = "MainActivity";
    AlarmCursorAdapter alarmCursorAdapter;

    ListView list_todo;

    private String alarmTitle = "";

    AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(this);

    public static final int VEHICLE_LOADER = 0;
    SharedPref sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);

        if (sharedPref.loadLightModeState() == true){
            setTheme(R.style.lightThemeApp);

        }
        else {
            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("AlarmReminder");
        */

        //ImageView starUnfilled = findViewById(R.id.starUnfilledTask);
        //ImageView starFilled = findViewById(R.id.starFilledTask);

        ImageView imgSettings = findViewById(R.id.imgSettings);
        ImageView bttnAddTodo = findViewById(R.id.bttnAddTodo);

        ImageView mintIconDark = findViewById(R.id.imgMintIcon);
        ImageView mintIconLight = findViewById(R.id.imgMintIconLight);

        if (sharedPref.loadLightModeState() == true){
            mintIconDark.setVisibility(View.INVISIBLE);
            mintIconLight.setVisibility(View.VISIBLE);
        }
        else {
            mintIconDark.setVisibility(View.VISIBLE);
            mintIconLight.setVisibility(View.INVISIBLE);
        }

        TextView tvDate = findViewById(R.id.tvDate);


        View emptyView = findViewById(R.id.emptyView);
        list_todo = findViewById(R.id.list_todo);
        list_todo.setEmptyView(emptyView);

        alarmCursorAdapter = new AlarmCursorAdapter(this, null);
        list_todo.setAdapter(alarmCursorAdapter);

        list_todo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);
                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, id);
                intent.setData(currentVehicleUri);
                startActivity(intent);
                overridePendingTransition(R.anim.slideleft2, R.anim.slideright2);


            }
        });



        imgSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slideleft2, R.anim.slideright2);
        });

        String dateYearDayMain = new SimpleDateFormat("E MMM dd, h:mm a", Locale.getDefault()).format(new Date());
        tvDate.setText(dateYearDayMain);

        bttnAddTodo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slideleft2, R.anim.slideright2);
            //addReminderTitle();
        });


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

        getSupportLoaderManager().initLoader(VEHICLE_LOADER, null, this);
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
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE,
        };

        return new CursorLoader(this, //Parent Activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        alarmCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        alarmCursorAdapter.swapCursor(null);
    }


    /*
    public void addReminderTitle(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Set Reminder Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setTextColor(Color.WHITE);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty()) {
                    return;
                }

                alarmTitle = input.getText().toString();
                ContentValues values = new ContentValues();

                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, alarmTitle);

                Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

                restartLoader();

                if (newUri == null) {
                    Toast.makeText(getApplicationContext(), "Setting reminder title failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Title set successfully", Toast.LENGTH_SHORT).show();
                }

            }

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
    */


    public void restartLoader(){
        getSupportLoaderManager().restartLoader(VEHICLE_LOADER, null, this);
    }

}
