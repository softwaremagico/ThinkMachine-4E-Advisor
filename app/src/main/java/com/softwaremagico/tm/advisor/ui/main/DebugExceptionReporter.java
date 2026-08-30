package com.softwaremagico.tm.advisor.ui.main;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.persistence.SettingsEntity;
import com.softwaremagico.tm.advisor.persistence.SettingsHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;

public final class DebugExceptionReporter {
    private static WeakReference<Activity> currentActivityRef = new WeakReference<>(null);

    private DebugExceptionReporter() {
    }

    public static void registerForegroundActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        currentActivityRef = new WeakReference<>(activity);
    }

    public static void unregisterForegroundActivity(Activity activity) {
        final Activity current = currentActivityRef.get();
        if (current == activity) {
            currentActivityRef.clear();
        }
    }

    public static void reportIfEnabled(String sourceClass, Throwable throwable) {
        if (throwable == null || !isDebugModeEnabled()) {
            return;
        }
        final Activity activity = currentActivityRef.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }

        activity.runOnUiThread(() -> {
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }

            final ScrollView scrollView = new ScrollView(activity);
            final int padding = dpToPx(activity, 12);
            scrollView.setPadding(padding, padding, padding, padding);
            final TextView textView = new TextView(activity);
            textView.setTypeface(Typeface.MONOSPACE);
            textView.setTextIsSelectable(true);
            textView.setTextSize(12);
            textView.setLineSpacing(0f, 1.2f);
            textView.setText(buildReadableTrace(sourceClass, throwable));
            scrollView.addView(textView);

            new AlertDialog.Builder(activity)
                    .setTitle(R.string.debug_exception_title)
                    .setView(scrollView)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        });
    }

    private static boolean isDebugModeEnabled() {
        final SettingsEntity settingsEntity = SettingsHandler.getSettingsEntity();
        return settingsEntity != null && settingsEntity.isDebugModeEnabled();
    }

    private static String buildReadableTrace(String sourceClass, Throwable throwable) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.println("Source: " + sourceClass);
        printWriter.println();
        throwable.printStackTrace(printWriter);
        printWriter.flush();
        return stringWriter.toString();
    }

    private static int dpToPx(Activity activity, int dp) {
        final float density = activity.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}

