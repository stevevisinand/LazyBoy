package com.hearc.stevevisinand.lazyboy;


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
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
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


    private int id;
    private String name;
    private List<Event> list_events;
    private List<Action> list_actions;
}