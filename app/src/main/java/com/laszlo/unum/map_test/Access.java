package com.laszlo.unum.map_test;

import android.app.Activity;
import android.app.Application;

/**
 * Created by Laszlo on 06/04/2015.
 */
public class Access extends Application
{
    public String[] array;

    public String[] getState()
    {
        return array;
    }
    public void setState(String[] input)
    {
        this.array = input;
    }
}
