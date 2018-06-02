package com.example.jyotidahal.saver;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView setVoice;

    //SMS
    int PERMISSION_TO_SEND_SMS = 1;
    String SENT ="SMS_SENT";
    String DELIVERED="SMS_DELIVER";
    PendingIntent sentPI,deliveredPI;
    BroadcastReceiver sentReciever, deliveredReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setVoice = (TextView) findViewById(R.id.setVoice);

        sentPI= PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        deliveredPI =PendingIntent.getBroadcast(this, 0,new Intent(DELIVERED),0);

    }

    public void receiveVoice(View view)
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); //its  take a input and send it to speech recong and return to intent
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);

        if(intent.resolveActivity(getPackageManager()) !=null)
        {
            startActivityForResult(intent, 20);
        }
        else
        {
            Toast.makeText(this,"Your device is not supportive",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case 20:
                if(resultCode == RESULT_OK && data != null)
                {
                  ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                  setVoice.setText(result.get(0));

                  if(result.get(0).equals("help me") || result.get(0).equals("help"))
                  {
                      sendSMS();
                      Toast.makeText(this,"Message sent successfully.",Toast.LENGTH_SHORT).show();
                  }
                  else{
                      Toast.makeText(this,"Message not Sent",Toast.LENGTH_SHORT).show();
                  }
                }


                break;
        }
    }


    //// sms

//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        sentReciever = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//            }
//        }
//    }

    public void sendSMS()
    {

        String message = "The person is in difficult situation. Help him";
        String number = "9808797408";

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_TO_SEND_SMS);
        }
        else
        {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(number,null,message,sentPI,deliveredPI);
        }
    }
}
