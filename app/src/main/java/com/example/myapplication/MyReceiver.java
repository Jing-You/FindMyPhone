package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import android.telephony.SmsManager;
import androidx.annotation.RequiresApi;
import android.media.MediaPlayer;
import android.media.AudioAttributes ;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileInputStream;
public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = MyReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "RECV successfully");
        // TODO: This method is called when the BroadcastReceiver is receiving
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String Adderess = "";
        String msg = "";
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show.
                Adderess = msgs[i].getOriginatingAddress();
                msg =  msgs[i].getMessageBody();
                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                strMessage += " :" + msgs[i].getMessageBody() + "\n";
                // Log and display the SMS message.
                Log.d(TAG, "onReceive: " + strMessage);
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
            }




        }
        /*
        MainActivity.getLocation();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String loc = MainActivity.getLocationStr();
        sendSMSMessage(Adderess, loc);

        try {
            MediaPlayer mediaPlayer = new MediaPlayer();

            File file = new File("/storage/emulated/0/Download/test.mp3");
            FileInputStream fis = new FileInputStream(file);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();
//            mediaPlayer.release();
//            mediaPlayer = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        if (msg.equals("SOS"))
        {
            MainActivity.getLocation();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String loc = MainActivity.getLocationStr();
            sendSMSMessage(Adderess, loc);
        }
        else if (msg.equals("Sound"))
        {
            try {
                MediaPlayer mediaPlayer = new MediaPlayer();

                File file = new File("/storage/emulated/0/Download/test.mp3");
                FileInputStream fis = new FileInputStream(file);
                mediaPlayer.setScreenOnWhilePlaying(true);
                mediaPlayer.setDataSource(fis.getFD());
                mediaPlayer.prepare(); // might take long! (for buffering, etc)
                mediaPlayer.start();
//            mediaPlayer.release();
//            mediaPlayer = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            Log.d(TAG, "Do nothing");


    }

    public void sendSMSMessage(String Adderess, String loc) {
        Log.i("Send SMS", loc);
        Log.i("Send SMS Adderess", Adderess);

        String phoneNo =  Adderess;
        String message = loc;


        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}