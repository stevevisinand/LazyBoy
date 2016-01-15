package com.hearc.stevevisinand.lazyboy.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.hearc.stevevisinand.lazyboy.Logic.Action;
import com.hearc.stevevisinand.lazyboy.Logic.ActionFactory;
import com.hearc.stevevisinand.lazyboy.Adapters.ActionAdapter;
import com.hearc.stevevisinand.lazyboy.Adapters.EventAdapter;
import com.hearc.stevevisinand.lazyboy.Logic.Action_launchApp;
import com.hearc.stevevisinand.lazyboy.Logic.Configuration;
import com.hearc.stevevisinand.lazyboy.Logic.Event;
import com.hearc.stevevisinand.lazyboy.Logic.EventFactory;
import com.hearc.stevevisinand.lazyboy.R;
import com.hearc.stevevisinand.lazyboy.Utilities.InterfaceUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;




public class ConfigurationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private static int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        //set system bar color
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

        mGoogleApiClient = new GoogleApiClient
                .Builder( this )

                .addApi( Places.GEO_DATA_API )
                .addApi( Places.PLACE_DETECTION_API )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .build();

        //init NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Context context = getApplicationContext();

        //Get the configuration passed in extras
        //erreur toast
        Toast errToast = Toast.makeText(context, getResources().getString(R.string.configurationActivity_ErrorLoading), Toast.LENGTH_LONG);

        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey("EXTRA_CONFIGURATION")) {
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
        InterfaceUtils.setListViewHeightBasedOnChildren(listViewActions);

        final EventAdapter adapterEvents = new EventAdapter(this, R.layout.list_layout_event, eventList);
        listViewEvents = (ListView) findViewById(R.id.listViewEvents);
        listViewEvents.setAdapter(adapterEvents);
        // fix to set listview in scollview
        InterfaceUtils.setListViewHeightBasedOnChildren(listViewEvents);


        //listeners
        final ConfigurationActivity activity = this;

        //Confirm button
        Button btnValid = (Button) findViewById(R.id.btnConfirm);
        btnValid.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //valid the changes and come back to the home
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


                                if(actions[which] == ActionFactory.ACTION_LAUNCHAPP)
                                {
                                    //advenced config
                                    selectApp(activity);
                                }
                                else
                                {
                                    actionList.add(ActionFactory.getAction(actions[which]));
                                }
                                listViewActions.invalidateViews();
                                // fix to set listview in scollview
                                InterfaceUtils.setListViewHeightBasedOnChildren(listViewActions);
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

                                //Advenced config here
                                if(events[which] == EventFactory.EVENT_WIFI_CONNECT)
                                {
                                    //HERE, THE USER SELECT THE WIFI
                                    selectWifi(activity);
                                }
                                else if(events[which] == EventFactory.EVENT_PLACE_DETECTER)
                                {
                                    selectPlace();
                                }
                                else if(events[which] == EventFactory.EVENT_NFC_DISCOVERED)
                                {
                                    setNFCTag();
                                }
                                else if(events[which] == EventFactory.EVENT_AC_CONNECTED)
                                {
                                    eventList.add(EventFactory.createACConnectEvent());
                                }


                                listViewEvents.invalidateViews();
                                // fix to set listview in scollview
                                InterfaceUtils.setListViewHeightBasedOnChildren(listViewEvents);
                            }
                        });

                builder.show();
            }
        });

    }

    private void selectApp(Activity activity)
    {

        Log.i("selectApp", "coucou");
        final PackageManager pm = getPackageManager();

        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        //use a treeMap to show values in alphabetical order
        TreeMap<String, String> appInfos = new TreeMap<>();
        for (ApplicationInfo packageInfo : packages) {
            appInfos.put(packageInfo.loadLabel(pm).toString(), packageInfo.packageName.toString());
        }

        String[] appnames1 = new String[appInfos.size()];
        String[] apppackages1 = new String[appInfos.size()];

        int i=0;
        for(Map.Entry<String,String> entry : appInfos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            appnames1[i] = key;
            apppackages1[i] = value;
            i++;
        }

        final String[] appnames = appnames1;
        final String[] apppackages = apppackages1;

        //Construct dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.configurationActivity_AddActionPopup_title)
                .setItems(appnames, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Action a = (Action)new Action_launchApp(appnames[which], apppackages[which]);
                        actionList.add(a);
                        listViewActions.invalidateViews();
                        // fix to set listview in scollview
                        InterfaceUtils.setListViewHeightBasedOnChildren(listViewActions);
                    }
                });

        builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /**
     * An user select a place and add it to the "eventlist"
     */
    private void selectPlace()
    {
        if( mGoogleApiClient == null || !mGoogleApiClient.isConnected() )
            return;

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        }
        catch(GooglePlayServicesNotAvailableException e){
            //TODO : advice user
            Log.i("Error-selectPlace", "GooglePlayServicesNotAvailableException");
        }
        catch(GooglePlayServicesRepairableException e){
            //TODO : advice user
            Log.i("Error-selectPlace", "GooglePlayServicesRepairableException");
        }
    }

    /**
     * Called when user select a place
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {

            //response from place picker
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                final double latitude = place.getLatLng().latitude;
                final double longitude = place.getLatLng().longitude;

                String addr = place.getAddress().toString();
                if(addr != ""){
                    addr = addr.split(",")[0];
                }

                final String address = addr;

                //Show dialog to select precision of position detecting

                //Create a personal dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();

                //Don't forget this to acces the view's elements
                final View view = inflater.inflate(R.layout.dialog_setperimeterlocation, null);

                final TextView TxtView_precision = (TextView)view.findViewById(R.id.txtView_perimeter);
                final SeekBar sbPerimeter = (SeekBar)view.findViewById(R.id.sb_perimeter);
                sbPerimeter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                        TxtView_precision.setText(progress + "m");
                    }
                    public void onStartTrackingTouch(SeekBar arg0) {
                        //rien
                    }
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        //rien
                    }

                });

                builder.setView(view)
                        .setMessage(R.string.dialog_SetPerimeter_body)
                        .setTitle(R.string.dialog_SetPerimeter_title)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //Add event
                                eventList.add(EventFactory.createPlaceEvent(address, latitude, longitude, sbPerimeter.getProgress()));

                                // fix to set listview in scollview
                                InterfaceUtils.setListViewHeightBasedOnChildren(listViewEvents);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

            }

        }
    }

    /**
     * An user select a wifi and add it to the "eventlist"
     */
    private void selectWifi(Activity activity)
    {
        //show popup wifi

        WifiManager managerWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        //to get configured network wifi need to be enable !
        //1. Enable Wifi if necessary
        boolean wifiState = managerWifi.isWifiEnabled();
        if(!wifiState) {
            managerWifi.setWifiEnabled(true);
        }

        //2. Get List of saved wifis
        List<WifiConfiguration> listWifis = managerWifi.getConfiguredNetworks();

        //3. Unenable Wifi if it was unactivated
        if(!wifiState){
            managerWifi.setWifiEnabled(false);
        }

        final String[] wifis = new String[listWifis.size()];
        int i = 0;
        for(WifiConfiguration config : listWifis){
            wifis[i] = config.SSID.toString();
            i++;
        }

        //Construct dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.configurationActivity_AddWifiPopup_title)
                .setItems(wifis, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Advenced config here
                        eventList.add(EventFactory.createWifiEvent(wifis[which]));

                        // fix to set listview in scollview
                        InterfaceUtils.setListViewHeightBasedOnChildren(listViewEvents);
                    }
                });

        builder.show();
    }

    private void setNFCTag()
    {
        if (nfcAdapter == null) {
            Toast.makeText(this, R.string.NFCnotSupported,
                    Toast.LENGTH_LONG).show();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, R.string.NFCunactivated,
                    Toast.LENGTH_LONG).show();
        }
        else {
            //NFC OK
            new AlertDialog.Builder(this)
                    .setTitle(R.string.configurationActivity_AddNFCTagPopup_title)
                    .setMessage(R.string.configurationActivity_AddNFCTagPopup_body)
                    .setPositiveButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //TODO : Stop the search

                                }
                            })
                    .create().show();

            //start reading
            handleIntent(getIntent());
        }
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
    protected void onResume() {
        super.onResume();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuration, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_edtName) { //change config name

            //Create a personal dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();

            //Don't forget this to acces the view's elements
            final View view = inflater.inflate(R.layout.dialog_addconfig, null);

            final EditText edtName = (EditText)view.findViewById(R.id.edtConfigName);

            //set text and force display keyboard on the edit text
            edtName.setText(this.config.getName());
            edtName.requestFocus();
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            builder.setView(view)
                    .setMessage(R.string.dialog_ChangeNameConfig)
                    .setTitle(R.string.dialog_ChangeNameConfig_Title)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Log.i("CHANGE_CONFIG_NAME", edtName.getText().toString());
                            config.setName(edtName.getText().toString());
                            setTitle(config.getName());

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

        // fix to set listview in scollview
        InterfaceUtils.setListViewHeightBasedOnChildren(listViewActions);
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

        // fix to set listview in scollview
        InterfaceUtils.setListViewHeightBasedOnChildren(listViewEvents);
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();

        Log.i("NFCTAG", "Start Searching");

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if ("text/plain".equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d("NFCTAG", "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
        else{
            Log.i("NFCTAG", "Not detected");
        }
    }



    private ArrayList<Action> actionList;
    private ArrayList<Event> eventList;
    private Configuration config;

    private ListView listViewActions;
    private ListView listViewEvents;


    private NfcAdapter nfcAdapter;






    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     *
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e("NdefReaderTask", "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Log.i("NFCREAD","Read content: " + result);
            }
        }
    }
}




