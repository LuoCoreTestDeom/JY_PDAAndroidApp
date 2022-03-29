package rcominfo.com.ejejyxt.Utils;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * Created by dell on 2016/12/30.
 */
public class VibratorUtil {

    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }
    public static void Vibrate(final Activity activity, long[] pattern,boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }
}
