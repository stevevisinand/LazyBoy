package com.hearc.stevevisinand.lazyboy.Logic;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

/**
 * Created by stevevisinand on 29.10.15.
 */
public abstract class Event implements Serializable
{
    public Event(String name)
    {
        this.name = name;
    }

    abstract boolean check(Context context, Intent intent, Double longitude, Double latitude);

    abstract public String getDescription();

    public String getName()
    {
        return this.name;
    }

    protected String name;
}
