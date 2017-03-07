package online.cagocapps.walkingthedog.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by Calvin on 3/5/2017.
 */

public class DbBitmapUtility {

    public static byte[] getBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image){
        if (image != null) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }
        else return null;
    }
}
