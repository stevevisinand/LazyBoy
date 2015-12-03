package com.hearc.stevevisinand.lazyboy.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hearc.stevevisinand.lazyboy.ActionFactory;
import com.hearc.stevevisinand.lazyboy.Action_wifi;
import com.hearc.stevevisinand.lazyboy.Configuration;
import com.hearc.stevevisinand.lazyboy.Adapters.ConfigurationAdapter;
import com.hearc.stevevisinand.lazyboy.EventFactory;
import com.hearc.stevevisinand.lazyboy.EventReceiver;
import com.hearc.stevevisinand.lazyboy.Event_nfc;
import com.hearc.stevevisinand.lazyboy.R;

import java.util.ArrayList;

public class LazyHome extends AppCompatActivity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy_home);

        setTitle("Lazy Boy");

        configurationList = new ArrayList<Configuration>();
        initList(configurationList);

        final ConfigurationAdapter adapter = new ConfigurationAdapter(this, R.layout.list_layout_config, configurationList);
        list = (ListView) findViewById(R.id.listView_configurations);

        list.setAdapter(adapter);


        //start EventReceiver
        EventReceiver e = new EventReceiver();
        this.registerReceiver(e,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.i("CHANGECONFIG", newConfig.toString());

    }


    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("restart", "---");
        //reload
        list.invalidateViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:

                //Create a personal dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = this.getLayoutInflater();

                //Don't forget this to acces the view's elements
                final View view = inflater.inflate(R.layout.dialog_addconfig, null);

                builder.setView(view)
                        .setMessage(R.string.dialog_AddNewConfig)
                        .setTitle(R.string.dialog_AddNewConfig_Title)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity

                                EditText edtName = (EditText)view.findViewById(R.id.edtConfigName);

                                Log.i("ADDCONFIG", edtName.getText().toString());
                                Configuration config = new Configuration(edtName.getText().toString());
                                addConfig(config);

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();

                dialog.show();

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lazy_home, menu);
        return true;
    }

    public void removeConfig(Configuration config)
    {
        configurationList.remove(config);

        //reload
        list.invalidateViews();
    }

    public void editConfig(Configuration config)
    {
        Log.i("Edit", config.getName());
        Intent intent = new Intent(LazyHome.this, ConfigurationActivity.class);
        intent.putExtra("EXTRA_CONFIGURATION", config);

        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Bundle res = data.getExtras();
        //res.getString("Email") ...
        Log.i("CommInterActivities", "onActivityResult");


        Bundle extras = data.getExtras();

        Configuration config = (Configuration) data.getSerializableExtra("EXTRA_CONFIGURATION_RESULT");
        //this.addConfig(config);

        Configuration original = null;
        for (Configuration c : configurationList) {
            if(c.getId() == config.getId())
            {
                original = c;
            }
        }

        if(original != null) {
            int i = configurationList.indexOf(original);
            configurationList.remove(i);
            configurationList.add(i, config);
        }
        else{
            Toast errToast = Toast.makeText(context, getResources().getString(R.string.LazyHome_ErrorChanging), Toast.LENGTH_LONG);
            errToast.show();
        }

    }

    public void addConfig(Configuration config)
    {
        configurationList.add(config);
        list.invalidateViews();
    }

    private void initList(ArrayList<Configuration> configs)
    {
        //TODO : change it to persistance
        Configuration config1 = new Configuration("Ecole");
        config1.addAction(ActionFactory.getAction(ActionFactory.ACTION_WIFI));
        config1.addEvent(EventFactory.getEvent(EventFactory.EVENT_NFC));
        configs.add(config1);

        Configuration config2 = new Configuration("Nuit");
        configs.add(config2);
    }

    private ArrayList<Configuration> configurationList;
    private ListView list;
}
