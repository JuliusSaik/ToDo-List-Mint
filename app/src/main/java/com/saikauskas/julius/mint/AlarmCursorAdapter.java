package com.saikauskas.julius.mint;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.saikauskas.julius.mint.data.AlarmReminderContract;

public class AlarmCursorAdapter extends CursorAdapter {

    private TextView mTitleText, mDateAndTimeText;
    private ImageView mActiveImage , mThumbnailImage;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

    public AlarmCursorAdapter(Context context, Cursor c) {
        super(context, c, 5/* flags */);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.task_items, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        mTitleText = view.findViewById(R.id.tvTitle);
        mDateAndTimeText = view.findViewById(R.id.tvDateAndTime);
        //mRepeatInfoText = view.findViewById(R.id.tvRepeatInfo);
        mThumbnailImage = view.findViewById(R.id.thumbnail_image);

        int titleColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
        int dateColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_DATE);
        int timeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TIME);
        //int repeatColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT);
        //int repeatNoColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
        //int repeatTypeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE);
        //int activeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE);

        String title = cursor.getString(titleColumnIndex);
        String date = cursor.getString(dateColumnIndex);
        String time = cursor.getString(timeColumnIndex);
        //String repeat = cursor.getString(repeatColumnIndex);
        //String repeatNo = cursor.getString(repeatNoColumnIndex);
        //String repeatType = cursor.getString(repeatTypeColumnIndex);
        //String active = cursor.getString(activeColumnIndex);




        setReminderTitle(title);

        if (date != null){
            String dateTime = date + " " + time;
            setReminderDateTime(dateTime);
        }else{
            mDateAndTimeText.setText("Date not set");
        }

        /*
        if(repeat != null){
            setReminderRepeatInfo(repeat, repeatNo, repeatType);
        }else{
            mRepeatInfoText.setText("Repeat Not Set");
        }
        */

      /*  if (active != null){
            setActiveImage(active);
        }else{
            mActiveImage.setImageResource(R.drawable.notify_off);
        }*/






    }

    // Set reminder title view
    public void setReminderTitle(String title) {
        mTitleText.setText(title);
        String letter = "A";

        if(title != null && !title.isEmpty()) {
            letter = title.substring(0, 1);
        }

        int color = mColorGenerator.getRandomColor();

        // Create a circular icon consisting of  a random background colour and first letter of title
        /*
        mDrawableBuilder = TextDrawable.builder()
                .buildRound(letter, color);
        mThumbnailImage.setImageDrawable(mDrawableBuilder);
        */
    }

    // Set date and time views
    public void setReminderDateTime(String datetime) {
        mDateAndTimeText.setText(datetime);
    }

    /*
    public void setReminderRepeatInfo(String repeat, String repeatNo, String repeatType) {
        if(repeat.equals("true")){
            mRepeatInfoText.setText("Every " + repeatNo + " " + repeatType + "(s)");
        }else if (repeat.equals("false")) {
            mRepeatInfoText.setText("Repeat Off");
        }
    }
    */

   /* // Set active image as on or off
    public void setActiveImage(String active){
        if(active.equals("true")){
            mActiveImage.setImageResource(R.drawable.notify_on);
        }else if (active.equals("false")) {
            mActiveImage.setImageResource(R.drawable.notify_off);
        }

    }*/
}
