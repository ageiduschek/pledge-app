package com.pledgeapp.pledge.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.widget.Toast;

import com.pledgeapp.pledge.R;

/**
 * A class for various utility functions
 */
public final class Util {
    public static void assertNotUIThread() {
        if ((Looper.getMainLooper().getThread() == Thread.currentThread())) {
            throw new RuntimeException("ASSERT NOT MAIN THREAD: FAILED");
        }
    }

    public static void assertUIThread() {
        if ((Looper.getMainLooper().getThread() != Thread.currentThread())) {
            throw new RuntimeException("ASSERT MAIN THREAD: FAILED");
        }
    }

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void displayNetworkErrorToast(Context context) {
        Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();

    }
}
