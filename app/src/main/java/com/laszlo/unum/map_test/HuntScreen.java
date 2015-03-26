package com.laszlo.unum.map_test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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
public class HuntScreen extends Activity
{
    //AQuery object
    AQuery aq;
    //list Object
    Button button;
    //list Spinner Ctrl Object
    ListView list;

    ProgressBar  newProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hunt);
        //Instantiate AQuery Object

        aq = new AQuery(this);

        button = (Button) findViewById(R.id.button);
        list = (ListView) findViewById(R.id.listView);
        list.setClickable(false);

        newProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        newProgressBar.setVisibility(View.GONE);

        //set listener on button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                select("select");
                newProgressBar.setVisibility(View.VISIBLE);
                //startActivity(new Intent(TestGet.this, MapsActivity.class));
            }
        });

        //set listener on listview
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Context context = getApplicationContext();
                CharSequence text = "Clicked";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context,text,duration);
                toast.show();
            }
        });


    }//ENDOFONCREATE


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
            //Set list adapter with created Java array 'values'
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getApplicationContext(),android.R.layout.simple_dropdown_item_1line, values);
            list.setAdapter(adapter);


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
                Toast.makeText(aq.getContext(),"Active connection required",Toast.LENGTH_SHORT).show();
            }
        }

    }
}