package com.softwaremagico.tm.advisor.ui.main;

import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.softwaremagico.tm.R;

public final class SnackbarGenerator {

    private SnackbarGenerator() {

    }

    public static Snackbar getInfoMessage(View view, int messageResource) {
        Snackbar snackbar = Snackbar.make(view, messageResource, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorInfo));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorInfoContainer));
        return snackbar;
    }

    public static Snackbar getInfoMessage(View view, int messageResource, int actionResource, View.OnClickListener action) {
        Snackbar snackbar = getInfoMessage(view, messageResource);
        snackbar.setAction(actionResource, action);
        return snackbar;
    }

    public static Snackbar getInfoMessage(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorOnInfo));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorOnInfoContainer));
        return snackbar;
    }

    public static Snackbar getWarningMessage(View view, int messageResource) {
        Snackbar snackbar = Snackbar.make(view, messageResource, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorOnWarning));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorOnWarningContainer));
        return snackbar;
    }

    public static Snackbar getWarningMessage(View view, int messageResource, int actionResource, View.OnClickListener action) {
        Snackbar snackbar = getWarningMessage(view, messageResource);
        snackbar.setAction(actionResource, action);
        return snackbar;
    }

    public static Snackbar getErrorMessage(View view, int messageResource) {
        Snackbar snackbar = Snackbar.make(view, messageResource, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.md_theme_onError));
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.md_theme_onErrorContainer));
        return snackbar;
    }

    public static Snackbar getErrorMessage(View view, int messageResource, int actionResource, View.OnClickListener action) {
        Snackbar snackbar = getErrorMessage(view, messageResource);
        snackbar.setAction(actionResource, action);
        return snackbar;
    }
}
