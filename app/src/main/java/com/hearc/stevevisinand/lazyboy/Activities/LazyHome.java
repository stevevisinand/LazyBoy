package com.hearc.stevevisinand.lazyboy.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hearc.stevevisinand.lazyboy.Logic.Configuration;
import com.hearc.stevevisinand.lazyboy.Adapters.ConfigurationAdapter;
import com.hearc.stevevisinand.lazyboy.EventService;
import com.hearc.stevevisinand.lazyboy.R;
import com.hearc.stevevisinand.lazyboy.Utilities.PersistanceUtils;


import java.util.ArrayList;

public class LazyHome extends AppCompatActivity {

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lazy_home);

        setTitle(" Lazy Boy");

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true); // <-- added
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //set system bar color
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }





        this.configurationList = new ArrayList<Configuration>();
        //load saved configurations or keep a created new list if empty
        try {
            this.configurationList = PersistanceUtils.loadConfigs(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        final ConfigurationAdapter adapter = new ConfigurationAdapter(this, R.layout.list_layout_config, configurationList);
        list = (ListView) findViewById(R.id.listView_configurations);

        list.setAdapter(adapter);

        //start Service
        reloadService();
    }


    /**
     * Relad Service and save configs to persistance
     */
    public void reloadService(){

        //save to persistance
        PersistanceUtils.saveConfigs(this.configurationList, this);

        //reload service
        stopService(new Intent(LazyHome.this, EventService.class));

        Intent serviceIntent = new Intent(LazyHome.this, EventService.class);
        startService(serviceIntent);
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

                //force display keyboard on the edit text
                final EditText edtName = (EditText)view.findViewById(R.id.edtConfigName);
                edtName.requestFocus();
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                builder.setView(view)
                        .setMessage(R.string.dialog_AddNewConfig)
                        .setTitle(R.string.dialog_AddNewConfig_Title)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Log.i("ADDCONFIG", edtName.getText().toString());
                                Configuration config = new Configuration(edtName.getText().toString());
                                addConfig(config);

                                //hide keyboard automatically
                                imm.hideSoftInputFromWindow(edtName.getWindowToken(), 0);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //hide keyboard automatically
                                imm.hideSoftInputFromWindow(edtName.getWindowToken(), 0);

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

    public void removeConfig(final Configuration config)
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_delete)
                .setTitle(R.string.dialog_DeleteConfirmation_title)
                .setMessage(getString(R.string.dialog_DeleteConfirmation_body) + " \"" + config.getName() + "\" ?")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        configurationList.remove(config);

                        //reload
                        list.invalidateViews();

                        //restart service
                        reloadService();
                    }

                })
                .setNegativeButton(R.string.cancel, null)
                .show();
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
            //configuration edited succesfully
            int i = configurationList.indexOf(original);
            configurationList.remove(i);
            configurationList.add(i, config);

            //reload service
            reloadService();
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

        //restart Sevice
        reloadService();
    }

    private ArrayList<Configuration> configurationList;
    private ListView list;
}
