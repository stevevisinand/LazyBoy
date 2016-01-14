package com.hearc.stevevisinand.lazyboy.Logic;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by stevevisinand on 29.10.15.
 */
public class Action_audioVolume extends Action{

    public Action_audioVolume(String name)
    {
        super(name);
    }

    public void apply(Context ctx)
    {
        AudioManager audioManager = (AudioManager) ctx.getSystemService(ctx.AUDIO_SERVICE);

        audioManager.setStreamVolume(AudioManager.STREAM_RING, 255, 0);

        Log.i("Action", "Action_audioVolume : Volume applied to " + 255);
    }

    public boolean isActiv(Context ctx)
    {
        return false;
    }


}