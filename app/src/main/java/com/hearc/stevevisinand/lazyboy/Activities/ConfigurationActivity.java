package com.hearc.stevevisinand.lazyboy.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.hearc.stevevisinand.lazyboy.Action;
import com.hearc.stevevisinand.lazyboy.ActionFactory;
import com.hearc.stevevisinand.lazyboy.Adapters.ActionAdapter;
import com.hearc.stevevisinand.lazyboy.Adapters.EventAdapter;
import com.hearc.stevevisinand.lazyboy.Configuration;
import com.hearc.stevevisinand.lazyboy.Event;
import com.hearc.stevevisinand.lazyboy.EventFactory;
import com.hearc.stevevisinand.lazyboy.R;
import com.hearc.stevevisinand.lazyboy.Utility;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        Context context = getApplicationContext();

        //Get the configuration passed in extras
        //erreur toast
        Toast errToast = Toast.makeText(context, getResources().getString(R.string.configurationActivity_ErrorLoading), Toast.LENGTH_LONG);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            //Stop activity
            errToast.show();
            this.finish(); //stop
        }
        else if(!extras.containsKey("EXTRA_CONFIGURATION")){
            //Stop activity
            errToast.show();
            this.finish(); //stop
        }

        //Set configuration source
        this.config = (Configuration)extras.get("EXTRA_CONFIGURATION");

        //set title
        setTitle(config.getName());

        //set lists
        actionList = (ArrayList) this.config.getActions();
        eventList = (ArrayList) this.config.getEvents();

        final ActionAdapter adapterActions = new ActionAdapter(this, R.layout.list_layout_action, actionList);
        listViewActions = (ListView) findViewById(R.id.listViewActions);
        listViewActions.setAdapter(adapterActions);
        // fix to set listview in scollview
        Utility.setListViewHeightBasedOnChildren(listViewActions);

        final EventAdapter adapterEvents = new EventAdapter(this, R.layout.list_layout_event, eventList);
        listViewEvents = (ListView) findViewById(R.id.listViewEvents);
        listViewEvents.setAdapter(adapterEvents);
        // fix to set listview in scollview
        Utility.setListViewHeightBasedOnChildren(listViewEvents);



        //listeners
        final ConfigurationActivity activity = this;

        //Confirm button
        Button btnValid = (Button) findViewById(R.id.btnConfirm);
        btnValid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //valid the changes and comme back to the home
                finishSetup();
                activity.finish();
            }
        });

        //Add Action button
        final String[] actions = ActionFactory.getActionsCatalog();

        Button btnAddAction = (Button) findViewById(R.id.btnAddAction);
        btnAddAction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Construct dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.configurationActivity_AddActionPopup_title)
                        .setItems(actions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //TODO : Advenced config here
                                actionList.add(ActionFactory.getAction(actions[which]));
                                listViewActions.invalidateViews();
                                // fix to set listview in scollview
                                Utility.setListViewHeightBasedOnChildren(listViewActions);
                            }
                });

                builder.show();
            }
        });

        //Add Event button
        final String[] events = EventFactory.getEventsCatalog();
        Button btnAddEvent = (Button) findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Construct dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.configurationActivity_AddEventPopup_title)
                        .setItems(events, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //TODO : Advenced config here
                                eventList.add(EventFactory.getEvent(events[which]));
                                listViewEvents.invalidateViews();
                                // fix to set listview in scollview
                                Utility.setListViewHeightBasedOnChildren(listViewEvents);
                            }
                        });

                builder.show();
            }
        });

    }

    private void finishSetup()
    {
        Intent intent = new Intent();
        //intent.putExtras(conData);
        intent.putExtra("EXTRA_CONFIGURATION_RESULT", this.config);

        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onPause() {
        Log.i("CommInterActivities", "onPause()");
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.i("CommInterActivities", "onBackPressed()");

        //don't forget to pass the config
        finishSetup();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_edtName) {

            //Create a personal dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();

            //Don't forget this to acces the view's elements
            final View view = inflater.inflate(R.layout.dialog_addconfig, null);

            builder.setView(view)
                    .setMessage(R.string.dialog_ChangeNameConfig)
                    .setTitle(R.string.dialog_ChangeNameConfig_Title)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity

                            EditText edtName = (EditText)view.findViewById(R.id.edtConfigName);


                            Log.i("CHANGE_CONFIG_NAME", edtName.getText().toString());
                            config.setName(edtName.getText().toString());
                            setTitle(config.getName());
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
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Remove an Action to the list of the config
     * @param action
     */
    public void removeAction(Action action)
    {
        actionList.remove(action);

        //reload
        listViewActions.invalidateViews();
    }

    /**
     * Remove an Event to the list of the config
     * @param event
     */
    public void removeEvent(Event event)
    {
        eventList.remove(event);

        //reload
        listViewEvents.invalidateViews();
    }

    private ArrayList<Action> actionList;
    private ArrayList<Event> eventList;
    private Configuration config;

    private ListView listViewActions;
    private ListView listViewEvents;
}
