package com.laszlo.unum.map_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Laszlo on 25/03/2015.
 */
public class TestGet extends Activity
{
    //AQuery object
    AQuery aq;
    //listCtrl Object
    Button buttonCtrl;
    //list Spinner Ctrl Object
    Spinner listCtrl;
    //Progress bar Object
    ProgressBar  newProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);
        //Instantiate AQuery Object

        aq = new AQuery(this);

        //button.setOnClickListener();

        buttonCtrl = (Button) findViewById(R.id.button);
        listCtrl = (Spinner) findViewById(R.id.spinner);
        newProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        newProgressBar.setVisibility(View.GONE);

        buttonCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(TestGet.this, MapsActivity.class));
                select("select");
                newProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

        public void callMap()
        {
            listCtrl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    startActivity(new Intent(TestGet.this, MapsActivity.class));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });

        }



    public void select(String options)
    {
        //JSON URL
        String url = "http://laszlo-malina.com/App/downloadHunt.php?options="+ options;
        //Make Asynchronous call using AJAX method
        aq.progress(newProgressBar).ajax(url, JSONObject.class, this,"jsonCallback");
    }
    public void jsonCallback(String url, JSONObject json, AjaxStatus status)
    {
        //When JSON is not null
        if (json != null)
        {
            String[] values = null;
            //Create GSON object
            Gson gson = new GsonBuilder().create();
            try
            {
                //Get JSON response by converting JSONArray into String
                String jsonResponse = json.getJSONArray("List").toString();
                //Using fromJson method deserialize JSON response [Convert JSON array into Java array]
                values = gson.fromJson(jsonResponse, String[].class);
            }
            catch (JSONException e)
            {
                // TODO Auto-generated catch block
                Toast.makeText(aq.getContext(), "Error in parsing JSON", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(aq.getContext(), "Cannot convert into Java Array", Toast.LENGTH_LONG).show();
            }
            //Set City adapter with created Java array 'values'
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getApplicationContext(),android.R.layout.simple_dropdown_item_1line, values);
            listCtrl.setAdapter(adapter);
            callMap();

        }
        //When JSON is null
        else
        {
            //When response code is 500 (Internal Server Error)
            if(status.getCode() == 500)
            {
                Toast.makeText(aq.getContext(),"There is an error in your php file!",Toast.LENGTH_SHORT).show();
            }
            //When response code is 404 (Not found)
            else if(status.getCode() == 404)
            {
                Toast.makeText(aq.getContext(),"Cannot read the php file!",Toast.LENGTH_SHORT).show();
            }
            //When response code is other than 500 or 404
            else
            {
                Toast.makeText(aq.getContext(),"Unknown Error occured!",Toast.LENGTH_SHORT).show();
            }
        }

    }
}