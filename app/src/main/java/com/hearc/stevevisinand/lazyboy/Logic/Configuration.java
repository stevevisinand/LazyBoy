package com.hearc.stevevisinand.lazyboy.Logic;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevevisinand on 24.09.15.
 */

//Serializable to pass it in "Extras" of intents
public class Configuration implements Serializable
{
    static int count=0;

    public Configuration(String name)
    {
        count ++;
        this.id = count;
        this.name = name;
        this.list_events = new ArrayList<Event>();
        this.list_actions = new ArrayList<Action>();
        this.isEnabled = true;
        this.activ = false;
    }

    public boolean needGPS()
    {
        for (Event e: list_events) {
            if(e instanceof Event_placeDetect)
            {
                return true;
            }
        }
        return false;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setEnable(boolean enable)
    {
        this.isEnabled = enable;
    }

    public boolean isEnable()
    {
        return this.isEnabled;
    }

    public void setName(String newName)
    {
        this.name = newName;
    }

    public boolean addEvent(Event event)
    {
        return this.list_events.add(event);
    }
    public boolean removeEvent(Event event)
    {
        return this.list_events.remove(event);
    }

    public boolean addAction(Action action)
    {
        return this.list_actions.add(action);
    }
    public boolean removeAction(Action action)
    {
        return this.list_actions.remove(action);
    }

    public List<Event> getEvents()
    {
        return list_events;
    }
    public List<Action> getActions()
    {
        return list_actions;
    }

    public List<String> getActionsNames()
    {
        List<String> list_str= new ArrayList<String>();
        for (Action a: list_actions) {
            list_str.add(a.getName());
        }
        return list_str;
    }

    public List<String> getEventsNames()
    {
        List<String> list_str= new ArrayList<String>();
        for (Event e: list_events) {
            list_str.add(e.getName());
        }
        return list_str;
    }

    public boolean isActionsActiv(Context context) {

        //check actions
        boolean activs = true;
        for (Action action : list_actions) {
            activs &= action.isActiv(context);
        }
        return activs;
    }

    public boolean isActiv(){
        return this.activ;
    }

    public boolean isEventsProduced(Context context, Intent intent, Double latitude, Double longitude){

        boolean produced = true;
        for (Event event : list_events) {
            produced &= event.check(context, intent, longitude, latitude);
        }
        return produced;
    }

    public void check(Context context, Intent intent, Double longitude, Double latitude)
    {
        //all event must be true !
        boolean produced = isEventsProduced(context, intent, latitude, longitude);

        this.activ = isActionsActiv(context);

        if(produced && !this.activ) {
            Toast toast = Toast.makeText(context, "Configuration : " + this.name + " Produced !", Toast.LENGTH_LONG);
            toast.show();

            for(Action action : list_actions){
                action.apply(context);
            }

            this.activ = true;
        }
        else{
            this.activ = false;
        }
    }

    private int id;
    private String name;
    private List<Event> list_events;
    private List<Action> list_actions;
    private boolean isEnabled;
    private boolean activ;

}