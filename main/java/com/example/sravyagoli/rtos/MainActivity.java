package com.example.sravyagoli.rtos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import java.util.UUID;
public class MainActivity extends AppCompatActivity
{
    Button b1;
    private static MainActivity instance;
    private static Context mContext;
    public static String uniqueID = UUID.randomUUID().toString();
    public static MainActivity getInstance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        b1 = (Button)findViewById(R.id.button);

        final Intent intent = new Intent(this , MyService.class);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(MyService.started == false )
                {
                    startService(intent);
                }
                else
                {
                    stopService(intent);
                    startService(intent);
                }

            }
        });
    }
}
