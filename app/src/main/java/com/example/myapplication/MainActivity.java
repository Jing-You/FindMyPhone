

package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ///123
    private static final int MY_PERMISSION_REQWUEST_RECEIVE_SMS = 0;
    static MainActivity instance;

    FusedLocationProviderClient fusedLocationProviderClient;
    static String locationStr = "https://www.google.com/maps?q=25.019583,121.541639";

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;
        // Ask permission for SMS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        getLocation();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getLocationStr();
                //sendSMSMessage("0930188127", "test");
            }
        });

    }

    public FusedLocationProviderClient getFusedLocationProviderClient() {
        return fusedLocationProviderClient;
    }

    @SuppressLint("MissingPermission")
    public static void getLocation() {
        Log.d("DEBUG", "get location");
        FusedLocationProviderClient fusedLocationProviderClient = instance.getFusedLocationProviderClient();
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                // Got last known location. In some rare situations this can be null.
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(instance, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        double lat = addresses.get(0).getLatitude();
                        double lon = addresses.get(0).getLongitude();
                        String country = addresses.get(0).getCountryName();
                        //locationStr = "My Country : " + country + ", latitude : " + Double.toString(lat) + ", longitude : " + Double.toString(lon);
                        String tmp = Double.toString(lat) + "," +  Double.toString(lon);
                        locationStr = "https://www.google.com/maps?q="+ tmp;
                        //Toast.makeText(instance, locationStr, Toast.LENGTH_LONG).show();
                        //Log.d("DEBUG", locationStr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    static public String getLocationStr() {
        Log.d("DEBUG", locationStr);
        Toast.makeText(instance, locationStr, Toast.LENGTH_LONG).show();
        return locationStr;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


