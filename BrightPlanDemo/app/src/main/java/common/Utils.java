package common;

import android.content.res.AssetManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static String loadJSONFromFile(AssetManager assetManager, String filename) {
        String json = null;
        try {
            InputStream is = assetManager.open(filename);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
