package com.hearc.stevevisinand.lazyboy.Utilities;

import android.content.Context;
import android.util.Log;

import com.hearc.stevevisinand.lazyboy.Logic.Configuration;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevevisinand on 05.01.16.
 */
public class PersistanceUtils {

    public static String FILENAME_SAVECONFIGS = "LazyBoy_configs";

    public static boolean saveConfigs(List<Configuration> list, Context context)
    {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME_SAVECONFIGS, Context.MODE_PRIVATE);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(list);

            oos.close();
            bos.close();
            fos.close();

            Log.i("PERSISTANCE", "Saving finished");

            return true;
        }
        catch(Exception e)
        {
            Log.i("PERSISTANCE", "Error saving : " + e);
            e.printStackTrace();

            return false;
        }
    }

    public static ArrayList<Configuration> loadConfigs(Context context) throws IOException, ClassNotFoundException
    {
        ArrayList<Configuration> configs;

        FileInputStream fis = context.openFileInput(FILENAME_SAVECONFIGS);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);

        configs = (ArrayList<Configuration>) ois.readObject();

        ois.close();
        bis.close();
        fis.close();
        Log.i("PERSISTANCE", "Loading finished");

        return configs;
    }


}
