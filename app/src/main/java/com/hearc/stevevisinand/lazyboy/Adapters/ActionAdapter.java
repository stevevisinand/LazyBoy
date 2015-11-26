package com.hearc.stevevisinand.lazyboy.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hearc.stevevisinand.lazyboy.Action;
import com.hearc.stevevisinand.lazyboy.Activities.ConfigurationActivity;
import com.hearc.stevevisinand.lazyboy.Activities.LazyHome;
import com.hearc.stevevisinand.lazyboy.R;

import java.util.ArrayList;

/**
 * Created by stevevisinand on 05.11.15.
 */
public class ActionAdapter extends ArrayAdapter<Action>
{
    public ActionAdapter(Context context, int textViewResourceId, ArrayList<Action> actions)
    {
        super(context, textViewResourceId, actions);

        this.actionsList = actions;

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
        final Action action = actionsList.get(position);

        //Set Apparence, infos
        if (action != null)
        {
            final TextView title = (TextView) view.findViewById(R.id.titleAction);
            title.setText(action.getName());
        }

        //Create BTNs Listners
        Button btnDelete = (Button)view.findViewById(R.id.btnDeleteAction);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Call home to remove from the list
                ConfigurationActivity activ = (ConfigurationActivity) context;
                activ.removeAction(action);
            }
        });

        return view;
    }


    private ArrayList<Action> actionsList;
    private Context context;
    private int viewRes;
    private Resources res;
}
