package com.example.sravyagoli.rtos;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import java.net.InetAddress;
import java.io.IOException;
import java.lang.Thread;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MyService extends Service {
    private static String TAG = "message";
    WifiManager wifiManager = (WifiManager) MainActivity.getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
    private String bssid;
    public static Boolean started = false;
    public void onCreate()
    {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        bssid = wifiInfo.getBSSID();
    }
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub

        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        // TODO Auto-generated method stub
        Log.d(TAG, "MyService started");
        started = true;
        new Thread(new Runnable() {
            public void run() {
                try {
                    String link = "http://ec2-54-89-87-35.compute-1.amazonaws.com/send_data.php?uid=1&bssid="+bssid;
                   // Log.d("hello","hi");
                    URL url = new URL(link);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                   // Log.d("data1:", String.valueOf(data));
                    while (data != -1) {
                        char current = (char) data;
                        data = isw.read();
                      //  System.out.println(current);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (true) {
                   wifiManager = (WifiManager) MainActivity.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                    String changedbssid = wifiInfo.getBSSID();

                    if (changedbssid==null) {
                        changedbssid = "null";
                    }
                    if (!bssid.equals(changedbssid)) {
                        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo WifiI = connManager.getActiveNetworkInfo();
                        Log.d("changed BSSID", changedbssid);
                        Log.d("UUID",MainActivity.uniqueID);
                        try {
                            InetAddress ipAddr = InetAddress.getByName("google.com");
                            Log.d("ip addr:",String.valueOf(ipAddr));
                            if(null!=ipAddr)
                            {
                                try {
                                    String uid = MainActivity.uniqueID;
                                    String link = "http://ec2-54-89-87-35.compute-1.amazonaws.com/send_data.php?uid=1&bssid="+changedbssid;
                               //     Log.d("hello","hi");
                                    URL url = new URL(link);
                                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                                    InputStream in = urlConnection.getInputStream();

                                    InputStreamReader isw = new InputStreamReader(in);

                                    int data = isw.read();
                                //    Log.d("data1:", String.valueOf(data));
                                    while (data != -1) {
                                        char current = (char) data;
                                        data = isw.read();
                               //         System.out.println(current);
                                    }
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                bssid = changedbssid;
                            }
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }).start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.d(TAG, "MyService destroyed");
        super.onDestroy();

    }
}
