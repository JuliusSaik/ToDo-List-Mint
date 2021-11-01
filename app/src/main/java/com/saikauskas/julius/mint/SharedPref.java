package com.saikauskas.julius.mint;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    SharedPreferences sharedPreferences;

    //recieves the App context
    public SharedPref(Context context){
        sharedPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    //this method will save the Light mode state : True or False
    public void setNightModeState(Boolean state){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LightMode", state);
        editor.commit();
    }
    //this method will load the Light mode State
    public Boolean loadLightModeState (){
        Boolean state = sharedPreferences.getBoolean("LightMode", false);
        return state;
    }

}
