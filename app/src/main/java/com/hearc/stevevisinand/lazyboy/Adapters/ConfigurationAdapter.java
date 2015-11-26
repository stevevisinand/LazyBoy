package com.hearc.stevevisinand.lazyboy.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hearc.stevevisinand.lazyboy.Activities.LazyHome;
import com.hearc.stevevisinand.lazyboy.Configuration;
import com.hearc.stevevisinand.lazyboy.R;

import java.util.ArrayList;

/**
 * Created by stevevisinand on 24.09.15.
 */
public class ConfigurationAdapter extends ArrayAdapter<Configuration>
{
    public ConfigurationAdapter(Context context, int textViewResourceId, ArrayList<Configuration> configurations)
    {
        super(context, textViewResourceId, configurations);

        this.configurationsList = configurations;

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
        final Configuration configuration = configurationsList.get(position);

        //Set Apparence, infos
        if (configuration != null)
        {
            final TextView title = (TextView) view.findViewById(R.id.title);

            //final String versionName = String.format(res.getString(R.string.list_title), androidVersion.getVersionName());
            title.setText(configuration.getName());
            //final String versionNumber = String.format(res.getString(R.string.list_desc), androidVersion.getVersionNumber());
        }

        //Create BTNs Listners
        Button btnDelete = (Button)view.findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Call home to remove from the list
                LazyHome lazyHome = (LazyHome) context;
                lazyHome.removeConfig(configuration);
            }
        });

        Button btnEdit = (Button)view.findViewById(R.id.btnEdit);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Call home to remove from the list
                LazyHome lazyHome = (LazyHome) context;
                lazyHome.editConfig(configuration);
            }
        });


        return view;
    }


    private ArrayList<Configuration> configurationsList;
    private Context context;
    private int viewRes;
    private Resources res;

}
