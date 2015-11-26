package com.hearc.stevevisinand.lazyboy;

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

    abstract void check();

    public String getName()
    {
        return this.name;
    }

    private String name;
}
