package com.sait.tawajudpremiumplusnewfeatured.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.sait.tawajudpremiumplusnewfeatured.R;

public class SnackBarUtil {
    private static AlertDialog currentAlertDialog;

    /**
     * Snackbar with no action Button and Default Background (Default One)
     **/





    public static void showSnackbar(Context context, String message, boolean isLengthLong) {
/*
        try {
            Snackbar snack = Snackbar.make(
                    (((AppCompatActivity) context).findViewById(android.R.id.content)),
                    message + "", Snackbar.LENGTH_SHORT);
            if (isLengthLong)
                snack.setDuration(Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = view
                    .findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
*/


        try {
//            new CustomDialogClass(context, message).show();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View alertDialogView = null;
            if (inflater != null) {
                alertDialogView = inflater.inflate(R.layout.custom_dialog_, null);
                alertDialog.setView(alertDialogView);
                TextView textDialog = alertDialogView.findViewById(R.id.diag_text);
                textDialog.setText(message);
                Button diag_ok = alertDialogView.findViewById(R.id.diag_ok);
                final AlertDialog show = alertDialog.show();
                show.setCancelable(false);
                Window window = show.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //TODO Dialog Size
                    show.getWindow().setLayout(((getWidth(context) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
                    show.getWindow().setGravity(Gravity.CENTER);
                }
                diag_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show.dismiss();
                    }
                });
            }
//            Intent intent = new Intent(context, CustomDialogClass.class);
//            intent.putExtra("message", message);
//            context.startActivity(intent);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }



    }

    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static void showSnackbar_optional_Drawable_backroundcolor(
            Fragment fragment,
            String message,
            int bgColor,
            String captureButtonText,
            String uploadButtonText,
            OnClickListener captureClickListener,
            OnClickListener uploadClickListener) {

        try {
            Context context = fragment.getContext();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View alertDialogView = null;
            if (inflater != null) {
                alertDialogView = inflater.inflate(R.layout.custom_dialog_for_capture_option, null);
                alertDialog.setView(alertDialogView);

                TextView textDialog = alertDialogView.findViewById(R.id.diag_text_heading);
                TextView text_capture_image = alertDialogView.findViewById(R.id.txt_capture_img);
                TextView text_upload_file = alertDialogView.findViewById(R.id.txt_uploadfile);

                textDialog.setText(message);

                TextView diag_ok = alertDialogView.findViewById(R.id.diag_ok);

                ImageView btnCaptureImage = alertDialogView.findViewById(R.id.btn_capture_image);
                ImageView btnUploadFile = alertDialogView.findViewById(R.id.btn_upload_file);

                diag_ok.setBackgroundColor(bgColor);

                // Set the capture and upload button text and click listeners
                text_capture_image.setText(captureButtonText);
                text_upload_file.setText(uploadButtonText);

                btnCaptureImage.setOnClickListener(captureClickListener);
                btnUploadFile.setOnClickListener(uploadClickListener);

                currentAlertDialog = alertDialog.show();
                currentAlertDialog.setCancelable(false);

                Window window = currentAlertDialog.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //TODO Dialog Size
                    currentAlertDialog.getWindow().setLayout(((getWidth(context) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
                    currentAlertDialog.getWindow().setGravity(Gravity.CENTER);
                }
                // Set onClick for the OK button
                diag_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentAlertDialog.dismiss();
                    }
                });
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
    // Method to dismiss the current dialog
    public static void dismissCurrentDialog() {
        if (currentAlertDialog != null && currentAlertDialog.isShowing()) {
            currentAlertDialog.dismiss();
            currentAlertDialog = null;
        }
    }
    public static void showSnackbar(Context context, String message) {
        try {
//            new CustomDialogClass(context, message).show();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View alertDialogView = null;
            if (inflater != null) {
                alertDialogView = inflater.inflate(R.layout.custom_dialog_, null);
                alertDialog.setView(alertDialogView);
                TextView textDialog = alertDialogView.findViewById(R.id.diag_text);
                textDialog.setText(message);
                Button diag_ok = alertDialogView.findViewById(R.id.diag_ok);
                final AlertDialog show = alertDialog.show();
                show.setCancelable(false);
                Window window = show.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //TODO Dialog Size
                    show.getWindow().setLayout(((getWidth(context) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
                    show.getWindow().setGravity(Gravity.CENTER);
                }
                diag_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show.dismiss();
                    }
                });
            }
//            Intent intent = new Intent(context, CustomDialogClass.class);
//            intent.putExtra("message", message);
//            context.startActivity(intent);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }



    @SuppressLint("ResourceAsColor")
    public static void showSnackbar_Drawable_backroundcolor(Context context, String message, int drawable_icon, int bgColor) {
        try {
//            new CustomDialogClass(context, message).show();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View alertDialogView = null;
            if (inflater != null) {
                alertDialogView = inflater.inflate(R.layout.custom_dialog_, null);
                alertDialog.setView(alertDialogView);
                TextView textDialog = alertDialogView.findViewById(R.id.diag_text);
                textDialog.setText(message);
                TextView diag_ok = alertDialogView.findViewById(R.id.diag_ok);

                ImageView icon = alertDialogView.findViewById(R.id.icon);

                String resourceName = context.getResources().getResourceEntryName(drawable_icon);
                Log.e("resourceName",resourceName);
                if(resourceName.contains("caution")) {
                    icon.setImageResource(drawable_icon);
                }
                diag_ok.setBackgroundColor(bgColor);

                final AlertDialog show = alertDialog.show();
                show.setCancelable(false);

                Window window = show.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //TODO Dialog Size
                    show.getWindow().setLayout(((getWidth(context) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
                    show.getWindow().setGravity(Gravity.CENTER);
                }
                diag_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show.dismiss();
                    }
                });
            }
//            Intent intent = new Intent(context, CustomDialogClass.class);
//            intent.putExtra("message", message);
//            context.startActivity(intent);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }



    @SuppressLint("ResourceAsColor")
    public static void showSnackbar_Drawable_backroundcolor(Context context, String message, int drawable_icon, int bgColor,  OnClickListenerNew listener) {
        try {
//            new CustomDialogClass(context, message).show();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View alertDialogView = null;
            if (inflater != null) {
                alertDialogView = inflater.inflate(R.layout.custom_dialog_, null);
                alertDialog.setView(alertDialogView);
                TextView textDialog = alertDialogView.findViewById(R.id.diag_text);
                textDialog.setText(message);
                TextView diag_ok = alertDialogView.findViewById(R.id.diag_ok);

                ImageView icon = alertDialogView.findViewById(R.id.icon);
              //  icon.setImageResource(drawable_icon);

                String resourceName = context.getResources().getResourceEntryName(drawable_icon);
                Log.e("resourceName",resourceName);
                if(resourceName.contains("caution")) {
                    icon.setImageResource(drawable_icon);
                }
                diag_ok.setBackgroundColor(bgColor);


                final AlertDialog show = alertDialog.show();
                show.setCancelable(false);

                Window window = show.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //TODO Dialog Size
                    show.getWindow().setLayout(((getWidth(context) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
                    show.getWindow().setGravity(Gravity.CENTER);
                }


                diag_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener != null) {
                            listener.onPositiveClick();
                        }
                        show.dismiss();
                    }
                });
            }
//            Intent intent = new Intent(context, CustomDialogClass.class);
//            intent.putExtra("message", message);
//            context.startActivity(intent);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }



    public interface OnClickListenerNew {
        void onPositiveClick();
    }

    public static void showSnackbar_ActionButton(Context context, String message) {
        try {
//            new CustomDialogClass(context, message).show();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View alertDialogView = null;
            if (inflater != null) {
                alertDialogView = inflater.inflate(R.layout.custom_dialog_, null);
                alertDialog.setView(alertDialogView);
                TextView textDialog = alertDialogView.findViewById(R.id.diag_text);
                textDialog.setText(message);
                Button diag_ok = alertDialogView.findViewById(R.id.diag_ok);
                final AlertDialog show = alertDialog.show();
                show.setCancelable(false);

                Window window = show.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //TODO Dialog Size
                    show.getWindow().setLayout(((getWidth(context) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
                    show.getWindow().setGravity(Gravity.CENTER);
                }
                diag_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show.dismiss();
                    }
                });
            }
//            Intent intent = new Intent(context, CustomDialogClass.class);
//            intent.putExtra("message", message);
//            context.startActivity(intent);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public static void showSnackbarWithIcon(Context context, String message, int drawable_icon) {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View alertDialogView = null;
            if (inflater != null) {
                alertDialogView = inflater.inflate(R.layout.custom_dialog_, null);
                alertDialog.setView(alertDialogView);

                TextView textDialog = alertDialogView.findViewById(R.id.diag_text);
                textDialog.setText(message);

                Button diag_ok = alertDialogView.findViewById(R.id.diag_ok);
                ImageView icon = alertDialogView.findViewById(R.id.icon);
                String resourceName = context.getResources().getResourceEntryName(drawable_icon);
                Log.e("resourceName",resourceName);
                if(resourceName.contains("caution")) {
                    icon.setImageResource(drawable_icon);
                }
                final AlertDialog show = alertDialog.show();
                show.setCancelable(false);

                Window window = show.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //TODO Dialog Size
                    show.getWindow().setLayout(((getWidth(context) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
                    show.getWindow().setGravity(Gravity.CENTER);
                }
                diag_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show.dismiss();
                    }
                });
            }

//            Intent intent = new Intent(context, CustomDialogClass.class);
//            intent.putExtra("message", message);
//            context.startActivity(intent);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }


    public static void showSnackbar(Context context, String message, int withoutIcon) {
        try {
//            new CustomDialogClass(context, message).show();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View alertDialogView = null;
            if (inflater != null) {
                alertDialogView = inflater.inflate(R.layout.custom_dialog_without_icon, null);
                alertDialog.setView(alertDialogView);

                TextView textDialog = alertDialogView.findViewById(R.id.diag_text);
                textDialog.setText(message);

                Button diag_ok = alertDialogView.findViewById(R.id.diag_ok);

                final AlertDialog show = alertDialog.show();
                show.setCancelable(false);

                Window window = show.getWindow();
                if (window != null) {
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //TODO Dialog Size
                    show.getWindow().setLayout(((getWidth(context) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
                    show.getWindow().setGravity(Gravity.CENTER);
                }
                diag_ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show.dismiss();
                    }
                });
            }

//            Intent intent = new Intent(context, CustomDialogClass.class);
//            intent.putExtra("message", message);
//            context.startActivity(intent);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Snackbar with no action Button and Custom Background
     **/
    @SuppressLint("WrongConstant")
    public static void showSnackbar(Context context, String message, int bgColor,
                                    boolean isLengthLong) {
        try {
            Snackbar snack = Snackbar.make(
                    (((AppCompatActivity) context).findViewById(android.R.id.content)),
                    message + "", Snackbar.LENGTH_SHORT);
            if (isLengthLong)
                snack.setDuration(Snackbar.LENGTH_LONG);
            View view = snack.getView();
            view.setBackgroundColor(bgColor);
            TextView tv = view
                    .findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        } catch (Exception ex) {

        }

    }

    /**
     * Snackbar with action Button and Custom Background
     **/
    public static void showSnackbar(Context context, String message,
                                    int bgColor, String actionButton, boolean isLengthLong,
                                    OnClickListener actionButtonClickListener) {
        try {
            Snackbar snack = Snackbar.make(
                    (((AppCompatActivity) context).findViewById(android.R.id.content)),
                    message + "", Snackbar.LENGTH_SHORT);
            if (isLengthLong)
                snack.setDuration(Snackbar.LENGTH_INDEFINITE);

            snack.setAction(actionButton, actionButtonClickListener);

            View view = snack.getView();
            view.setBackgroundColor(bgColor);
            TextView tv = view
                    .findViewById(com.google.android.material.R.id.snackbar_text);

            TextView tvAction = view
                    .findViewById(com.google.android.material.R.id.snackbar_action);
            tvAction.setTextSize(16);
            tvAction.setTextColor(Color.WHITE);

            tv.setTextColor(Color.WHITE);
            snack.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Snackbar with action Button and Default Background
     **/
    public static void showSnackbar(Context context, String message,
                                    String actionButton, boolean isLengthLong,
                                    OnClickListener actionButtonClickListener) {
        try {
            Snackbar snack = Snackbar.make(
                    (((AppCompatActivity) context).findViewById(android.R.id.content)),
                    message + "", Snackbar.LENGTH_SHORT);
            if (isLengthLong)
                snack.setDuration(Snackbar.LENGTH_INDEFINITE);
            snack.setAction(actionButton, actionButtonClickListener);
            View view = snack.getView();
            TextView tv = view
                    .findViewById(com.google.android.material.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);

            TextView tvAction = view
                    .findViewById(com.google.android.material.R.id.snackbar_action);
            tvAction.setTextSize(16);
            tvAction.setTextColor(Color.WHITE);

            snack.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




}// main
