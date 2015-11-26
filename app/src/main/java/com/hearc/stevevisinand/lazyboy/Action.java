package com.hearc.stevevisinand.lazyboy;

import java.io.Serializable;

/**
 * Created by stevevisinand on 29.10.15.
 */
public abstract class Action implements Serializable {

    public Action(String name)
    {
        this.name = name;
    }

    abstract void apply();

    public String getName()
    {
        return this.name;
    }

    private String name;
}
