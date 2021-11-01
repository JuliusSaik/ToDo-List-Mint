package com.saikauskas.julius.mint;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class SettingsActivity extends AppCompatActivity {

    String addresses, subject, attachment;
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
        setContentView(R.layout.activity_settings);

        Switch lightThemeSwitch = findViewById(R.id.lightThemeSwitch);

        //when you check the switch it checks if the App is light theme
        if (sharedPref.loadLightModeState() == true){
            lightThemeSwitch.setChecked(true);
        }

        lightThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //check if the button is checked or not
                if (isChecked){
                    sharedPref.setNightModeState(true);
                    restartApp();
                }
                //if it is not checked stay in dark mode don't change to light mode
                else {
                    sharedPref.setNightModeState(false);
                    restartApp();
                }
            }
        });






        ImageView ivBackArrowSettings = findViewById(R.id.ivBackArrowSettings);

        ImageView tvPressDevInsta = findViewById(R.id.tvPressDevInsta);
        ImageView ivPressFeatureSuggest = findViewById(R.id.ivPressFeatureSuggest);
        ImageView ivReportBug = findViewById(R.id.ivReportBug);

        ivBackArrowSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slideright, R.anim.slideleft);
            }
        });

        tvPressDevInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDevInsta();
            }
        });

        ivPressFeatureSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","juliusaikauskas@gmail.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feature for Mint.");
                intent.putExtra(Intent.EXTRA_TEXT, "Hey, I suggest you add a feature to your App where...");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        ivReportBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","juliusaikauskas@gmail.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Report a Bug for Mint.");
                intent.putExtra(Intent.EXTRA_TEXT, "Hey, I found a bug on your App...");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
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

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    private void openDevInsta() {
        Uri webpage = Uri.parse("https://www.instagram.com/juliussaik/");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
