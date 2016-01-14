package com.hearc.stevevisinand.lazyboy.Logic;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

/**
 * Created by stevevisinand on 29.10.15.
 */
public class Action_bluetooth extends Action{

    private boolean activ;

    public Action_bluetooth(String name, boolean activ)
    {
        super(name);
        this.activ = activ;
    }

    public void apply(Context ctx)
    {

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(activ) //enable bluetooth
        {
            if (!bluetoothAdapter.isEnabled())
            {
                bluetoothAdapter.enable();
            }
        }
        else //disable bluetooth
        {
            if (bluetoothAdapter.isEnabled())
            {
                bluetoothAdapter.disable();
            }
        }

        Log.i("Action", "Action_bluetooth " + this.activ);
    }

    public boolean isActiv(Context ctx)
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.isEnabled() == this.activ;
    }


}