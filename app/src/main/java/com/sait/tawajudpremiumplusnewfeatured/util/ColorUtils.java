package com.sait.tawajudpremiumplusnewfeatured.util;

import android.content.Context;
import android.util.TypedValue;

public class ColorUtils {

    public static String toHexColor(int color) {
        return String.format("#%08X", (0xFFFFFFFFL & color));
    }


    public static int resolveColorAttribute(Context context, int colorAttribute) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttribute, typedValue, true);
        return typedValue.data;
    }

}
