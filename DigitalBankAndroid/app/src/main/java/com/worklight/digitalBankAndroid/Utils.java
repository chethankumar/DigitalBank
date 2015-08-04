package com.worklight.digitalBankAndroid;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by chethan on 20/05/15.
 */
public class Utils  {

    public static Typeface getBoldTypeface(Context context) {
        return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/ProximaNova-Semibold.otf");
    }

    public static Typeface getProximaTypeface(Context context) {
        return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/ProximaNova-Regular.otf");
    }

    public static Typeface getLightTypeface(Context context) {
        return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/ProximaNova-Light.otf");
    }

}
