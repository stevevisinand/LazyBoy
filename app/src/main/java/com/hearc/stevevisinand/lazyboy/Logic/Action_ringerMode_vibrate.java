package com.hearc.stevevisinand.lazyboy.Logic;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by stevevisinand on 29.10.15.
 */
public class Action_ringerMode_vibrate extends Action{

    private AudioManager audioManager;

    public Action_ringerMode_vibrate(String name)
    {
        super(name);
    }

    public void apply(Context ctx)
    {
        audioManager = (AudioManager) ctx.getSystemService(ctx.AUDIO_SERVICE);

        audioManager.setRingerMode(audioManager.RINGER_MODE_VIBRATE);

        Log.i("Action", "Action_ringerMode_vibrate");
    }

    public boolean isActiv(Context ctx)
    {
        AudioManager audioManager = (AudioManager) ctx.getSystemService(ctx.AUDIO_SERVICE);
        return audioManager.getRingerMode() == audioManager.RINGER_MODE_VIBRATE;
    }


}