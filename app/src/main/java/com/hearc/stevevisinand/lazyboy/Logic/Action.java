package com.hearc.stevevisinand.lazyboy.Logic;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by stevevisinand on 29.10.15.
 */
public abstract class Action implements Serializable {

    public Action(String name)
    {
        this.name = name;
    }

    abstract void apply(Context ctx);

    public String getName()
    {
        return this.name;
    }

    abstract boolean isActiv(Context ctx);

    private String name;
}
