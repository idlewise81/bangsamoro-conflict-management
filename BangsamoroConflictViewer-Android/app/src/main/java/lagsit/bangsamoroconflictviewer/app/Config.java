package lagsit.bangsamoroconflictviewer.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;

public class Config {
    public static final String FIREBASE_URL = "https://bangsamoro-conflict-manager.firebaseio.com/";
    public static final String REF_CHILD = "ref_child";
    public static final String REF_LEVEL = "ref_level";
    public static final String REF_NAME = "ref_name";
    public static final String PREFS_NAME = "MyPrefs";
    public static final String REF_TYPE = "ref_type";


    public static void setRefType(Context context, String path) {

        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(REF_TYPE, path);
        editor.commit();
    }

    public static String getRefType(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(REF_TYPE, "Province");
    }


    public static void setRefName(Context context, String path) {

        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(REF_NAME, path);
        editor.commit();
    }

    public static String getRefName(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(REF_NAME, "Incidents\nby\nProvince");
    }

    public static void setRefChild(Context context, String path) {

        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(REF_CHILD, path);
        editor.commit();
    }

    public static String getRefChild(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(REF_CHILD, "gr_province_all");
    }

    public static void setRefLevel(Context context, int path) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt(REF_LEVEL, path);
        editor.commit();
    }

    public static int getRefLevel(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getInt(REF_LEVEL, 2);
    }

    public static void totals(String table, String field, final int newInt, final int op) {
        Firebase ref = new Firebase(FIREBASE_URL + table + "/" + field);

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(newInt);
                } else {
                    if (op == 1) {
                        currentData.setValue((Long) currentData.getValue() + newInt);
                    } else
                        currentData.setValue((Long) currentData.getValue() - newInt);
                }

                return Transaction.success(currentData);
            }

            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
                if (firebaseError != null) {
                    System.out.println("Firebase counter increment failed.");
                } else {
                    System.out.println("Firebase counter increment succeeded.");
                }
            }
        });
    }


}
