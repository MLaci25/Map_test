package com.laszlo.unum.map_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Unum on 23/03/2015.
 */
public class HuntScreen extends Activity
{

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);

        Button listHunt = (Button) findViewById(R.id.button);

        listHunt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(HuntScreen.this, MapsActivity.class));
            }
        });
    }
}
