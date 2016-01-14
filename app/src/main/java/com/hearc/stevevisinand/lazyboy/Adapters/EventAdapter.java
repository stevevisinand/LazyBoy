package com.hearc.stevevisinand.lazyboy.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hearc.stevevisinand.lazyboy.Activities.ConfigurationActivity;
import com.hearc.stevevisinand.lazyboy.Logic.Event;
import com.hearc.stevevisinand.lazyboy.R;

import java.util.ArrayList;

/**
 * Created by stevevisinand on 05.11.15.
 */
public class EventAdapter extends ArrayAdapter<Event>
{
    public EventAdapter(Context context, int textViewResourceId, ArrayList<Event> events)
    {
        super(context, textViewResourceId, events);

        this.eventsList = events;

        this.context = context;
        this.viewRes = textViewResourceId;
        this.res = context.getResources();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(viewRes, parent, false);
        }

        //this is the configuration
        final Event event = eventsList.get(position);

        //Set Apparence, infos
        if (event != null)
        {
            final TextView title = (TextView) view.findViewById(R.id.titleEvent);
            title.setText(event.getName());

            final TextView description = (TextView) view.findViewById(R.id.descriptionEvent);
            description.setText(event.getDescription());
        }

        //Create BTNs Listners
        Button btnDelete = (Button)view.findViewById(R.id.btnDeleteEvent);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ConfigurationActivity activ = (ConfigurationActivity) context;
                activ.removeEvent(event);
            }
        });

        return view;
    }


    private ArrayList<Event> eventsList;
    private Context context;
    private int viewRes;
    private Resources res;
}
