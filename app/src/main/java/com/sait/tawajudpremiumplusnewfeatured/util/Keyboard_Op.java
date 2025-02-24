package com.sait.tawajudpremiumplusnewfeatured.util;

import android.app.Service;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Keyboard_Op {

    public static void hide(Context context) {
        try {
            if (context != null) {
                InputMethodManager imm = (InputMethodManager) context
                        .getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((AppCompatActivity) context).getCurrentFocus()
                        .getWindowToken(), InputMethodManager.SHOW_FORCED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void hide(Context context, EditText et) {
        try {
            et.clearFocus();
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Service.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }
    public static void show(Context context, EditText et) {
        try {
            et.requestFocus();
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Service.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, 0);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        } catch (Exception e) {
        }
    }

}
